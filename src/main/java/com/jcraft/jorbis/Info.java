/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/* JOrbis
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *  
 * Written by: 2000 ymnk<ymnk@jcraft.com>
 *   
 * Many thanks to 
 *   Monty <monty@xiph.org> and 
 *   The XIPHOPHORUS Company http://www.xiph.org/ .
 * JOrbis has been based on their awesome works, Vorbis codec.
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;

public class Info{
  private static final int OV_EBADPACKET=-136;
  private static final int OV_ENOTAUDIO=-135;

  private static byte[] _vorbis="vorbis".getBytes();
  private static final int VI_TIMEB=1;
  //  private static final int VI_FLOORB=1;
  private static final int VI_FLOORB=2;
  //  private static final int VI_RESB=1;
  private static final int VI_RESB=3;
  private static final int VI_MAPB=1;
  private static final int VI_WINDOWB=1;

  public int version;
  public int channels;
  public int rate;

  // The below bitrate declarations are *hints*.
  // Combinations of the three values carry the following implications:
  //     
  // all three set to the same value: 
  // implies a fixed rate bitstream
  // only nominal set: 
  // implies a VBR stream that averages the nominal bitrate.  No hard 
  // upper/lower limit
  // upper and or lower set: 
  // implies a VBR bitstream that obeys the bitrate limits. nominal 
  // may also be set to give a nominal rate.
  // none set:
  //  the coder does not care to speculate.

  int bitrate_upper;
  int bitrate_nominal;
  int bitrate_lower;

  // Vorbis supports only short and long blocks, but allows the
  // encoder to choose the sizes

  int[] blocksizes=new int[2];

  // modes are the primary means of supporting on-the-fly different
  // blocksizes, different channel mappings (LR or mid-side),
  // different residue backends, etc.  Each mode consists of a
  // blocksize flag and a mapping (along with the mapping setup

  int modes;
  int maps;
  int times;
  int floors;
  int residues;
  int books;
  int psys; // encode only

  InfoMode[] mode_param=null;

  int[] map_type=null;
  Object[] map_param=null;

  int[] time_type=null;
  Object[] time_param=null;

  int[] floor_type=null;
  Object[] floor_param=null;

  int[] residue_type=null;
  Object[] residue_param=null;

  StaticCodeBook[] book_param=null;

  PsyInfo[] psy_param=new PsyInfo[64]; // encode only

  // for block long/sort tuning; encode only
  int envelopesa;
  float preecho_thresh;
  float preecho_clamp;

  // used by synthesis, which has a full, alloced vi
  public void init(){
    this.rate = 0;
  }

  public void clear(){
    for(int i = 0; i < this.modes; i++) {
      this.mode_param[i] = null;
    }
    this.mode_param = null;

    for(int i = 0; i < this.maps; i++) { // unpack does the range checking
      FuncMapping.mapping_P[this.map_type[i]].free_info(this.map_param[i]);
    }
    this.map_param = null;

    for(int i = 0; i < this.times; i++) { // unpack does the range checking
      FuncTime.time_P[this.time_type[i]].free_info(this.time_param[i]);
    }
    this.time_param = null;

    for(int i = 0; i < this.floors; i++) { // unpack does the range checking
      FuncFloor.floor_P[this.floor_type[i]].free_info(this.floor_param[i]);
    }
    this.floor_param = null;

    for(int i = 0; i < this.residues; i++) { // unpack does the range checking
      FuncResidue.residue_P[this.residue_type[i]].free_info(this.residue_param[i]);
    }
    this.residue_param = null;

    // the static codebooks *are* freed if you call info_clear, because
    // decode side does alloc a 'static' codebook. Calling clear on the
    // full codebook does not clear the static codebook (that's our
    // responsibility)
    for(int i = 0; i < this.books; i++) {
      // just in case the decoder pre-cleared to save space
      if(this.book_param[i] != null) {
        this.book_param[i].clear();
        this.book_param[i] = null;
      }
    }
    //if(vi->book_param)free(vi->book_param);
    this.book_param = null;

    for(int i = 0; i < this.psys; i++) {
      this.psy_param[i].free();
    }

  }

  // Header packing/unpacking
  int unpack_info(Buffer opb){
    this.version = opb.read(32);
    if(this.version != 0)
      return (-1);

    this.channels = opb.read(8);
    this.rate = opb.read(32);

    this.bitrate_upper = opb.read(32);
    this.bitrate_nominal = opb.read(32);
    this.bitrate_lower = opb.read(32);

    this.blocksizes[0] = 1 << opb.read(4);
    this.blocksizes[1] = 1 << opb.read(4);

    if((this.rate < 1) || (this.channels < 1) || (this.blocksizes[0] < 8) || (this.blocksizes[1] < this.blocksizes[0])
        ||(opb.read(1)!=1)){
      clear();
      return (-1);
    }
    return (0);
  }

  // all of the real encoding details are here.  The modes, books,
  // everything
  int unpack_books(Buffer opb){

    this.books = opb.read(8) + 1;

    if(this.book_param == null || this.book_param.length != this.books)
      this.book_param = new StaticCodeBook[this.books];
    for(int i = 0; i < this.books; i++) {
      this.book_param[i] = new StaticCodeBook();
      if(this.book_param[i].unpack(opb) != 0) {
        clear();
        return (-1);
      }
    }

    // time backend settings
    this.times = opb.read(6) + 1;
    if(this.time_type == null || this.time_type.length != this.times)
      this.time_type = new int[this.times];
    if(this.time_param == null || this.time_param.length != this.times)
      this.time_param = new Object[this.times];
    for(int i = 0; i < this.times; i++) {
      this.time_type[i] = opb.read(16);
      if(this.time_type[i] < 0 || this.time_type[i] >= VI_TIMEB) {
        clear();
        return (-1);
      }
      this.time_param[i] = FuncTime.time_P[this.time_type[i]].unpack(this, opb);
      if(this.time_param[i] == null) {
        clear();
        return (-1);
      }
    }

    // floor backend settings
    this.floors = opb.read(6) + 1;
    if(this.floor_type == null || this.floor_type.length != this.floors)
      this.floor_type = new int[this.floors];
    if(this.floor_param == null || this.floor_param.length != this.floors)
      this.floor_param = new Object[this.floors];

    for(int i = 0; i < this.floors; i++) {
      this.floor_type[i] = opb.read(16);
      if(this.floor_type[i] < 0 || this.floor_type[i] >= VI_FLOORB) {
        clear();
        return (-1);
      }

      this.floor_param[i] = FuncFloor.floor_P[this.floor_type[i]].unpack(this, opb);
      if(this.floor_param[i] == null) {
        clear();
        return (-1);
      }
    }

    // residue backend settings
    this.residues = opb.read(6) + 1;

    if(this.residue_type == null || this.residue_type.length != this.residues)
      this.residue_type = new int[this.residues];

    if(this.residue_param == null || this.residue_param.length != this.residues)
      this.residue_param = new Object[this.residues];

    for(int i = 0; i < this.residues; i++) {
      this.residue_type[i] = opb.read(16);
      if(this.residue_type[i] < 0 || this.residue_type[i] >= VI_RESB) {
        clear();
        return (-1);
      }
      this.residue_param[i] = FuncResidue.residue_P[this.residue_type[i]].unpack(this, opb);
      if(this.residue_param[i] == null) {
        clear();
        return (-1);
      }
    }

    // map backend settings
    this.maps = opb.read(6) + 1;
    if(this.map_type == null || this.map_type.length != this.maps)
      this.map_type = new int[this.maps];
    if(this.map_param == null || this.map_param.length != this.maps)
      this.map_param = new Object[this.maps];
    for(int i = 0; i < this.maps; i++) {
      this.map_type[i] = opb.read(16);
      if(this.map_type[i] < 0 || this.map_type[i] >= VI_MAPB) {
        clear();
        return (-1);
      }
      this.map_param[i] = FuncMapping.mapping_P[this.map_type[i]].unpack(this, opb);
      if(this.map_param[i] == null) {
        clear();
        return (-1);
      }
    }

    // mode settings
    this.modes = opb.read(6) + 1;
    if(this.mode_param == null || this.mode_param.length != this.modes)
      this.mode_param = new InfoMode[this.modes];
    for(int i = 0; i < this.modes; i++) {
      this.mode_param[i] = new InfoMode();
      this.mode_param[i].blockflag = opb.read(1);
      this.mode_param[i].windowtype = opb.read(16);
      this.mode_param[i].transformtype = opb.read(16);
      this.mode_param[i].mapping = opb.read(8);

      if((this.mode_param[i].windowtype >= VI_WINDOWB)
        || (this.mode_param[i].transformtype >= VI_WINDOWB)
        || (this.mode_param[i].mapping >= this.maps)) {
        clear();
        return (-1);
      }
    }

    if(opb.read(1)!=1){
      clear();
      return (-1);
    }

    return (0);
  }

  // The Vorbis header is in three packets; the initial small packet in
  // the first page that identifies basic parameters, a second packet
  // with bitstream comments and a third packet that holds the
  // codebook.

  public int synthesis_headerin(Comment vc, Packet op){
    Buffer opb=new Buffer();

    if(op!=null){
      opb.readinit(op.packet_base, op.packet, op.bytes);

      // Which of the three types of header is this?
      // Also verify header-ness, vorbis
      {
        byte[] buffer=new byte[6];
        int packtype=opb.read(8);
        opb.read(buffer, 6);
        if(buffer[0]!='v'||buffer[1]!='o'||buffer[2]!='r'||buffer[3]!='b'
            ||buffer[4]!='i'||buffer[5]!='s'){
          // not a vorbis header
          return (-1);
        }
        switch(packtype){
          case 0x01: // least significant *bit* is read first
            if(op.b_o_s==0){
              // Not the initial packet
              return (-1);
            }
            if(this.rate != 0) {
              // previously initialized info header
              return (-1);
            }
            return (unpack_info(opb));
          case 0x03: // least significant *bit* is read first
            if(this.rate == 0) {
              // um... we didn't get the initial header
              return (-1);
            }
            return (vc.unpack(opb));
          case 0x05: // least significant *bit* is read first
            if(this.rate == 0 || vc.vendor == null) {
              // um... we didn;t get the initial header or comments yet
              return (-1);
            }
            return (unpack_books(opb));
          default:
            // Not a valid vorbis header staticEntityType
            //return(-1);
            break;
        }
      }
    }
    return (-1);
  }

  // pack side
  int pack_info(Buffer opb){
    // preamble
    opb.write(0x01, 8);
    opb.write(_vorbis);

    // basic information about the stream
    opb.write(0x00, 32);
    opb.write(this.channels, 8);
    opb.write(this.rate, 32);

    opb.write(this.bitrate_upper, 32);
    opb.write(this.bitrate_nominal, 32);
    opb.write(this.bitrate_lower, 32);

    opb.write(Util.ilog2(this.blocksizes[0]), 4);
    opb.write(Util.ilog2(this.blocksizes[1]), 4);
    opb.write(1, 1);
    return (0);
  }

  int pack_books(Buffer opb){
    opb.write(0x05, 8);
    opb.write(_vorbis);

    // books
    opb.write(this.books - 1, 8);
    for(int i = 0; i < this.books; i++) {
      if(this.book_param[i].pack(opb) != 0) {
        //goto err_out;
        return (-1);
      }
    }

    // times
    opb.write(this.times - 1, 6);
    for(int i = 0; i < this.times; i++) {
      opb.write(this.time_type[i], 16);
      FuncTime.time_P[this.time_type[i]].pack(this.time_param[i], opb);
    }

    // floors
    opb.write(this.floors - 1, 6);
    for(int i = 0; i < this.floors; i++) {
      opb.write(this.floor_type[i], 16);
      FuncFloor.floor_P[this.floor_type[i]].pack(this.floor_param[i], opb);
    }

    // residues
    opb.write(this.residues - 1, 6);
    for(int i = 0; i < this.residues; i++) {
      opb.write(this.residue_type[i], 16);
      FuncResidue.residue_P[this.residue_type[i]].pack(this.residue_param[i], opb);
    }

    // maps
    opb.write(this.maps - 1, 6);
    for(int i = 0; i < this.maps; i++) {
      opb.write(this.map_type[i], 16);
      FuncMapping.mapping_P[this.map_type[i]].pack(this, this.map_param[i], opb);
    }

    // modes
    opb.write(this.modes - 1, 6);
    for(int i = 0; i < this.modes; i++) {
      opb.write(this.mode_param[i].blockflag, 1);
      opb.write(this.mode_param[i].windowtype, 16);
      opb.write(this.mode_param[i].transformtype, 16);
      opb.write(this.mode_param[i].mapping, 8);
    }
    opb.write(1, 1);
    return (0);
  }

  public int blocksize(Packet op){
    //codec_setup_info
    Buffer opb=new Buffer();

    int mode;

    opb.readinit(op.packet_base, op.packet, op.bytes);

    /* Check the packet staticEntityType */
    if(opb.read(1)!=0){
      /* Oops.  This is not an audio data packet */
      return (OV_ENOTAUDIO);
    }
    {
      int modebits=0;
      int v = this.modes;
      while(v>1){
        modebits++;
        v>>>=1;
      }

      /* read our mode and pre/post windowsize */
      mode=opb.read(modebits);
    }
    if(mode==-1)
      return (OV_EBADPACKET);
    return (this.blocksizes[this.mode_param[mode].blockflag]);
  }

  public String toString(){
    return "version:" + new Integer(this.version) + ", channels:" + new Integer(this.channels)
      + ", rate:" + new Integer(this.rate) + ", bitrate:" + new Integer(this.bitrate_upper)
      + "," + new Integer(this.bitrate_nominal) + "," + new Integer(this.bitrate_lower);
  }
}
