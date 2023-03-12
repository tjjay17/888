import os
import sys


def main():
    # walk through the  current directory
    for root, dirs, files in os.walk(os.getcwd()):
        # compile all .inp and .out files
        for file in files:
            print(file)
            if file.endswith(".inp") or file.endswith(".out"):
                # compile it into one file called "compiled.txt"
                with open("compiled.txt", "a") as compiled:
                    with open(os.path.join(root, file), "r") as f:
                        # write the file name and the content of the file
                        compiled.write("\n" + "="*10 + file + "="*10 + "\n")
                        compiled.write(f.read() + "\n\n")

if __name__ == "__main__":
    main()
