#!/usr/bin/env python
import sys
import os
import getopt
from PIL import Image
from Crypto.Cipher import AES
from Crypto import Random

def main(argv=None):
    """
    Ce script chiffre un fichier jpeg avec AES-256 en mode ECB ou CBC
    Options :
        -i, --input     fichier jpeg
        -m, --mode      ECB ou CBC
        -o, --out       fichier chiffre (facultatif)
    """
    if argv is None:
        argv = sys.argv
    try:
        opts, args = getopt.getopt(argv[1:], "hi:m:o:",
                                   ["help", "input=", "mode=", "out="])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        return 2
    # process options
    for o, a in opts:
        if o in ("-h", "--help"):
            print main.__doc__
            return 0

        elif o in ("-i", "--input"):
            in_filename = a

        elif o in ("-m", "--mode"):
            if a == 'ECB':
                mode = AES.MODE_ECB
            elif a == 'CBC':
                mode = AES.MODE_CBC
            else:
                print 'Erreur de syntaxe pour le mode'
                print main.__doc__
                return 1

        elif o in ("-o", "--out"):
            out_filename = a

    try:
        in_filename
        mode
    except:
        print 'Erreur de syntaxe'
        print main.__doc__
        return 1

    try:
        out_filename
    except:
        (filename, ext) = os.path.splitext(in_filename)
        out_filename = filename + '_enc' + ext

    if args:
        print main.__doc__
        return 1

    key = Random.new().read(AES.key_size[2])
    iv = Random.new().read(AES.block_size)
    if mode == AES.MODE_ECB :
	cipher = AES.new(key, mode)
    else : 
	cipher = AES.new(key, mode, iv)

    with open(in_filename) as infile:
        im = Image.open(infile)
        data = list(im.getdata())
        nb_comp = len(data[0])
        data_str = ''.join([''.join([chr(comp) for comp in pixel])
                            for pixel in data])
        nb_add = 0
        if len(data_str) % 16 != 0:
            nb_add = (16 - len(data_str) % 16)
            data_str += '\x00' * nb_add

        data_str_enc = cipher.encrypt(data_str)
        data_str_enc = data_str_enc[:-nb_add]

        data_enc = [tuple(map(ord, data_str_enc[i:i + nb_comp]))
                    for i in range(0, len(data_str_enc), nb_comp)]

        imNew = Image.new(im.mode, im.size)
        imNew.putdata(data_enc)
        imNew.save(out_filename)

if __name__ == '__main__':
    main()
