

# Cygwin and MSYS2/MinGW

Eclipse CDT 
ToolSet 和 LaunchConfiguration的参数需要传递给ProcessFactory，这样才能知道是Cygwin还是MinGW.
Import C/C++ executable时，需要能够确定是否是Cygwin还是MinGW，可以通过 BinaryParser分析文件来确定。


# Terminal Emulator

How to use gdb's --tty option to debug full-screen applications with detached gdb control #1187
https://github.com/mintty/mintty/issues/1187

Konsole need to implement openpty mode to support gdb's --tty option
https://bugs.kde.org/show_bug.cgi?id=462495

gnome-terminal need to implement openpty mode to support gdb's --tty option
https://gitlab.gnome.org/GNOME/gnome-terminal/-/issues/7951

How to fix an ancient GDB problem
https://lwn.net/Articles/909496/

# README.md
https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-readmes
If a repository contains more than one README file, then the file shown is chosen from locations in the following order: the .github directory, then the repository's root directory, and finally the docs directory.
