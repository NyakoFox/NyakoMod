
import sys
import os

name = sys.argv[1]

os.rename(name, "a" + name)

os.system("ffmpeg -i a" + name + " -ac 1 " + name)

os.remove("a" + name)