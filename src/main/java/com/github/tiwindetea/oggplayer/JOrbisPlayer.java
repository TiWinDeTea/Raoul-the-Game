package com.github.tiwindetea.oggplayer;
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/* JOrbisPlayer -- pure Java Ogg Vorbis player
 *
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *
 * Written by: 2000 ymnk<ymnk@jcraft.com>
 *
 * Many thanks to 
 *   Monty <monty@xiph.org> and 
 *   The XIPHOPHORUS Company http://www.xiph.org/ .
 * JOrbis has been based on their awesome works, Vorbis codec and
 * JOrbisPlayer depends on JOrbis.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 *
 * nb : This version was modified by Lucas LAZARE
 */

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class JOrbisPlayer implements Runnable {

    private ObservableThread player = null;
    private volatile boolean isLooping = false;

    private volatile boolean isRunning = false;

    InputStream bitStream = null;
    int udp_port = -1;

    String udp_baddress = null;
    static final int BUFSIZE = 4096 * 2;

    static int convsize = BUFSIZE * 2;
    static byte[] convbuffer = new byte[convsize];
    private int RETRY = 3;

    int retry = this.RETRY;
    InputStream file = null;
    SyncState oy;

    StreamState os;
    Page og;
    Packet op;
    Info vi;
    Comment vc;
    DspState vd;
    Block vb;
    byte[] buffer = null;

    int bytes = 0;
    int rate = 0;

    int channels = 0;
    SourceDataLine outputLine = null;
    String current_source = null;
    int frameSizeInBytes;

    int bufferLengthInBytes;

    public JOrbisPlayer(URL file) {
        this.file = selectSource(file);
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    private void init_jorbis() {
        this.oy = new SyncState();
        this.os = new StreamState();
        this.og = new Page();
        this.op = new Packet();

        this.vi = new Info();
        this.vc = new Comment();
        this.vd = new DspState();
        this.vb = new Block(this.vd);

        this.buffer = null;
        this.bytes = 0;

        this.oy.init();
    }

    private SourceDataLine getOutputLine(int channels, int rate) {
        if (this.outputLine == null || this.rate != rate || this.channels != channels) {
            if (this.outputLine != null) {
                this.outputLine.drain();
                this.outputLine.stop();
                this.outputLine.close();
            }
            init_audio(channels, rate);
            this.outputLine.start();
        }
        return this.outputLine;
    }

    private void init_audio(int channels, int rate) {

        try {
            AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, // PCM_Signed
                    false // littleEndian
            );
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat,
                    AudioSystem.NOT_SPECIFIED);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Sound not supported by system");
                return;
            }

            try {
                this.outputLine = (SourceDataLine) AudioSystem.getLine(info);
                this.outputLine.open(audioFormat);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
                return;
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                return;
            }

            this.frameSizeInBytes = audioFormat.getFrameSize();
            int bufferLengthInFrames = this.outputLine.getBufferSize() / this.frameSizeInBytes / 2;
            this.bufferLengthInBytes = bufferLengthInFrames * this.frameSizeInBytes;
            this.rate = rate;
            this.channels = channels;
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void close() {
        if (this.bitStream != null) {
            try {
                this.bitStream.close();
                this.bitStream = null;
                this.outputLine.drain();
                this.outputLine.stop();
                this.outputLine.close();
                this.outputLine = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        Thread me = Thread.currentThread();
        if (this.bitStream == null) {
            this.bitStream = this.file;
            this.bitStream.mark(Integer.MAX_VALUE);
        } else {
            try {
                this.bitStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.bitStream != null) {
            if (this.udp_port != -1) {
                play_udp_stream(me);
            } else {
                play_stream(me);
            }
        }
        this.player = null;
    }

    private void play_stream(Thread me) {

        boolean chained = false;

        init_jorbis();

        this.retry = this.RETRY;

        loop:
        while (true) {
            int eos = 0;

            int index = this.oy.buffer(BUFSIZE);
            this.buffer = this.oy.data;
            try {
                this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            this.oy.wrote(this.bytes);

            if (chained) {
                chained = false;
            } else {
                if (this.oy.pageout(this.og) != 1) {
                    if (this.bytes < BUFSIZE)
                        break;
                    System.err.println("Input does not appear to be an Ogg bitstream.");
                    return;
                }
            }
            this.os.init(this.og.serialno());
            this.os.reset();

            this.vi.init();
            this.vc.init();

            if (this.os.pagein(this.og) < 0) {
                // error; stream version mismatch perhaps
                System.err.println("Error reading first page of Ogg bitstream data.");
                return;
            }

            this.retry = this.RETRY;

            if (this.os.packetout(this.op) != 1) {
                // no page? must not be vorbis
                System.err.println("Error reading initial header packet.");
                break;
                //      return;
            }

            if (this.vi.synthesis_headerin(this.vc, this.op) < 0) {
                // error case; not a vorbis header
                System.err
                        .println("This Ogg bitstream does not contain Vorbis audio data.");
                return;
            }

            int i = 0;

            while (i < 2) {
                while (i < 2) {
                    int result = this.oy.pageout(this.og);
                    if (result == 0)
                        break; // Need more data
                    if (result == 1) {
                        this.os.pagein(this.og);
                        while (i < 2) {
                            result = this.os.packetout(this.op);
                            if (result == 0)
                                break;
                            if (result == -1) {
                                System.err.println("Corrupt secondary header.  Exiting.");
                                break loop;
                            }
                            this.vi.synthesis_headerin(this.vc, this.op);
                            i++;
                        }
                    }
                }

                index = this.oy.buffer(BUFSIZE);
                this.buffer = this.oy.data;
                try {
                    this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (this.bytes == 0 && i < 2) {
                    System.err.println("End of file before finding all Vorbis headers!");
                    return;
                }
                this.oy.wrote(this.bytes);
            }

            convsize = BUFSIZE / this.vi.channels;

            this.vd.synthesis_init(this.vi);
            this.vb.init(this.vd);

            float[][][] _pcmf = new float[1][][];
            int[] _index = new int[this.vi.channels];

            getOutputLine(this.vi.channels, this.vi.rate);

            while (eos == 0) {
                while (eos == 0) {

                    if (this.player != me) {
                        return;
                    }

                    int result = this.oy.pageout(this.og);
                    if (result == 0)
                        break;
                    if (result != -1) {
                        this.os.pagein(this.og);

                        if (this.og.granulepos() == 0) { //
                            chained = true; //
                            eos = 1; //
                            break; //
                        } //

                        while (true) {
                            result = this.os.packetout(this.op);
                            if (result == 0)
                                break; // need more data
                            if (result != -1) {
                                // we have a packet.  Decode it
                                int samples;
                                if (this.vb.synthesis(this.op) == 0) { // test for success!
                                    this.vd.synthesis_blockin(this.vb);
                                }
                                while ((samples = this.vd.synthesis_pcmout(_pcmf, _index)) > 0) {
                                    float[][] pcmf = _pcmf[0];
                                    int bout = (samples < convsize ? samples : convsize);

                                    // convert doubles to 16 bit signed ints (host order) and
                                    // interleave
                                    for (i = 0; i < this.vi.channels; i++) {
                                        int ptr = i * 2;
                                        //int ptr=i;
                                        int mono = _index[i];
                                        for (int j = 0; j < bout; j++) {
                                            int val = (int) (pcmf[i][mono + j] * 32767.);
                                            if (val > 32767) {
                                                val = 32767;
                                            }
                                            if (val < -32768) {
                                                val = -32768;
                                            }
                                            if (val < 0)
                                                val = val | 0x8000;
                                            convbuffer[ptr] = (byte) (val);
                                            convbuffer[ptr + 1] = (byte) (val >>> 8);
                                            ptr += 2 * (this.vi.channels);
                                        }
                                    }
                                    this.outputLine.write(convbuffer, 0, 2 * this.vi.channels * bout);
                                    this.vd.synthesis_read(bout);
                                }
                            }
                        }
                        if (this.og.eos() != 0)
                            eos = 1;
                    }
                }

                if (eos == 0) {
                    index = this.oy.buffer(BUFSIZE);
                    this.buffer = this.oy.data;
                    try {
                        this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
                    } catch (Exception e) {
                        System.err.println(e);
                        return;
                    }
                    if (this.bytes == -1) {
                        break;
                    }
                    this.oy.wrote(this.bytes);
                    if (this.bytes == 0)
                        eos = 1;
                }
            }

            this.os.clear();
            this.vb.clear();
            this.vd.clear();
            this.vi.clear();
        }

        this.oy.clear();
    }

    private void play_udp_stream(Thread me) {
        init_jorbis();

        try {
            loop:
            while (true) {
                int index = this.oy.buffer(BUFSIZE);
                this.buffer = this.oy.data;
                try {
                    this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
                } catch (Exception e) {
                    System.err.println(e);
                    return;
                }

                this.oy.wrote(this.bytes);
                if (this.oy.pageout(this.og) != 1) {
                    System.err.println("Input does not appear to be an Ogg bitstream.");
                    return;
                }

                this.os.init(this.og.serialno());
                this.os.reset();

                this.vi.init();
                this.vc.init();
                if (this.os.pagein(this.og) < 0) {
                    // error; stream version mismatch perhaps
                    System.err.println("Error reading first page of Ogg bitstream data.");
                    return;
                }

                if (this.os.packetout(this.op) != 1) {
                    // no page? must not be vorbis
                    System.err.println("Error reading initial header packet.");
                    return;
                }

                if (this.vi.synthesis_headerin(this.vc, this.op) < 0) {
                    // error case; not a vorbis header
                    System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
                    return;
                }

                int i = 0;
                while (i < 2) {
                    while (i < 2) {
                        int result = this.oy.pageout(this.og);
                        if (result == 0)
                            break; // Need more data
                        if (result == 1) {
                            this.os.pagein(this.og);
                            while (i < 2) {
                                result = this.os.packetout(this.op);
                                if (result == 0)
                                    break;
                                if (result == -1) {
                                    System.err.println("Corrupt secondary header.  Exiting.");
                                    break loop;
                                }
                                this.vi.synthesis_headerin(this.vc, this.op);
                                i++;
                            }
                        }
                    }

                    if (i == 2)
                        break;

                    index = this.oy.buffer(BUFSIZE);
                    this.buffer = this.oy.data;
                    try {
                        this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (this.bytes == 0 && i < 2) {
                        System.err.println("End of file before finding all Vorbis headers!");
                        return;
                    }
                    this.oy.wrote(this.bytes);
                }
                break;
            }
        } catch (Exception e) {
        }

        UDPIO io = null;
        try {
            io = new UDPIO(this.udp_port);
        } catch (Exception e) {
            return;
        }

        this.bitStream = io;
        play_stream(me);
    }

    public void play_sound() {
        if (this.player == null) {
            this.isRunning = true;
            this.player = new ObservableThread(this);
            if (this.isLooping) {
                this.player.setListener(new TerminatedThreadHandler() {
                    @Override
                    public void handle() {
                        if (JOrbisPlayer.this.isRunning) {
                            JOrbisPlayer.this.player = new ObservableThread(JOrbisPlayer.this);
                            JOrbisPlayer.this.player.setListener(this);
                            JOrbisPlayer.this.player.start();
                        }
                    }
                });
            } else {
                this.player.setListener(new TerminatedThreadHandler() {
                    @Override
                    public void handle() {
                        JOrbisPlayer.this.stop_sound();
                    }
                });
            }
            this.player.start();
        }
    }

    public void stop_sound() {
        this.player = null;
        this.isRunning = false;
    }

    InputStream selectSource(URL url) {
        String item = url.toString();
        if (item.endsWith(".pls")) {
            item = fetch_pls(item);
        } else if (item.endsWith(".m3u")) {
            item = fetch_m3u(item);
        }

        if (item == null || !item.endsWith(".ogg")) {
            return null;
        }

        InputStream is = null;
        URLConnection urlc = null;
        try {
            urlc = url.openConnection();
            is = urlc.getInputStream();
            this.current_source = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort()
                    + url.getFile();
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (is == null) {
            this.current_source = null;
            return null;
        }

        int i = 0;
        String s, t;
        this.udp_port = -1;
        this.udp_baddress = null;
        while (urlc != null) {
            s = urlc.getHeaderField(i);
            t = urlc.getHeaderFieldKey(i);
            if (s == null)
                break;
            i++;
            if (t != null) {
                if (t.equals("udp-port")) {
                    try {
                        this.udp_port = Integer.parseInt(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (t.equals("udp-broadcast-address")) {
                    this.udp_baddress = s;
                }
            }
        }
        return is;
    }

    String fetch_pls(String pls) {
        InputStream pstream = null;
        if (pls.startsWith("http://")) {
            try {
                URL url = new URL(pls);
                URLConnection urlc = url.openConnection();
                pstream = urlc.getInputStream();
            } catch (Exception ee) {
                ee.printStackTrace();
                return null;
            }
        }
        if (pstream == null) {
            try {
                pstream = new FileInputStream(System.getProperty("user.dir")
                        + System.getProperty("file.separator") + pls);
            } catch (Exception ee) {
                ee.printStackTrace();
                return null;
            }
        }

        String line = null;
        while (true) {
            try {
                line = readline(pstream);
            } catch (Exception e) {
            }
            if (line == null)
                break;
            if (line.startsWith("File1=")) {
                byte[] foo = line.getBytes();
                int i = 6;
                for (; i < foo.length; i++) {
                    if (foo[i] == 0x0d)
                        break;
                }
                return line.substring(6, i);
            }
        }
        return null;
    }

    String fetch_m3u(String m3u) {
        InputStream pstream = null;
        if (m3u.startsWith("http://")) {
            try {
                URL url = new URL(m3u);
                URLConnection urlc = url.openConnection();
                pstream = urlc.getInputStream();
            } catch (Exception ee) {
                ee.printStackTrace();
                return null;
            }
        }
        if (pstream == null) {
            try {
                pstream = new FileInputStream(System.getProperty("user.dir")
                        + System.getProperty("file.separator") + m3u);
            } catch (Exception ee) {
                ee.printStackTrace();
                return null;
            }
        }

        String line = null;
        while (true) {
            try {
                line = readline(pstream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (line == null)
                break;
            return line;
        }
        return null;
    }

    private String readline(InputStream is) {
        StringBuffer rtn = new StringBuffer();
        int temp;
        do {
            try {
                temp = is.read();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (temp == -1) {
                String str = rtn.toString();
                if (str.length() == 0)
                    return null;
                return str;
            }
            if (temp != 0 && temp != '\n' && temp != '\r')
                rtn.append((char) temp);
        }
        while (temp != '\n' && temp != '\r');
        return rtn.toString();
    }

    class UDPIO extends InputStream {
        InetAddress address;
        DatagramSocket socket = null;
        DatagramPacket sndpacket;
        DatagramPacket recpacket;
        byte[] buf = new byte[1024];
        //String host;
        int port;
        byte[] inbuffer = new byte[2048];
        byte[] outbuffer = new byte[1024];
        int instart = 0, inend = 0, outindex = 0;

        UDPIO(int port) {
            this.port = port;
            try {
                this.socket = new DatagramSocket(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.recpacket = new DatagramPacket(this.buf, 1024);
        }

        void setTimeout(int i) {
            try {
                this.socket.setSoTimeout(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int getByte() throws java.io.IOException {
            if ((this.inend - this.instart) < 1) {
                read(1);
            }
            return this.inbuffer[this.instart++] & 0xff;
        }

        int getByte(byte[] array) throws java.io.IOException {
            return getByte(array, 0, array.length);
        }

        int getByte(byte[] array, int begin, int length) throws java.io.IOException {
            int i = 0;
            int foo = begin;
            while (true) {
                if ((i = (this.inend - this.instart)) < length) {
                    if (i != 0) {
                        System.arraycopy(this.inbuffer, this.instart, array, begin, i);
                        begin += i;
                        length -= i;
                        this.instart += i;
                    }
                    read(length);
                    continue;
                }
                System.arraycopy(this.inbuffer, this.instart, array, begin, length);
                this.instart += length;
                break;
            }
            return begin + length - foo;
        }

        int getShort() throws java.io.IOException {
            if ((this.inend - this.instart) < 2) {
                read(2);
            }
            int s = 0;
            s = this.inbuffer[this.instart++] & 0xff;
            s = ((s << 8) & 0xffff) | (this.inbuffer[this.instart++] & 0xff);
            return s;
        }

        int getInt() throws java.io.IOException {
            if ((this.inend - this.instart) < 4) {
                read(4);
            }
            int i = 0;
            i = this.inbuffer[this.instart++] & 0xff;
            i = ((i << 8) & 0xffff) | (this.inbuffer[this.instart++] & 0xff);
            i = ((i << 8) & 0xffffff) | (this.inbuffer[this.instart++] & 0xff);
            i = (i << 8) | (this.inbuffer[this.instart++] & 0xff);
            return i;
        }

        void getPad(int n) throws java.io.IOException {
            int i;
            while (n > 0) {
                if ((i = this.inend - this.instart) < n) {
                    n -= i;
                    this.instart += i;
                    read(n);
                    continue;
                }
                this.instart += n;
                break;
            }
        }

        void read(int n) throws java.io.IOException {
            if (n > this.inbuffer.length) {
                n = this.inbuffer.length;
            }
            this.instart = this.inend = 0;
            int i;
            while (true) {
                this.recpacket.setData(this.buf, 0, 1024);
                this.socket.receive(this.recpacket);

                i = this.recpacket.getLength();
                System.arraycopy(this.recpacket.getData(), 0, this.inbuffer, this.inend, i);
                if (i == -1) {
                    throw new java.io.IOException();
                }
                this.inend += i;
                break;
            }
        }

        public void close() throws java.io.IOException {
            this.socket.close();
        }

        public int read() throws java.io.IOException {
            return 0;
        }

        public int read(byte[] array, int begin, int length)
                throws java.io.IOException {
            return getByte(array, begin, length);
        }
    }
}
