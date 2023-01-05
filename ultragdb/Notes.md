


# Planned changes

## Replace

```
Runtime.getRuntime().exec(args);
ProcessFactory.getFactory().exec(args);
```

## Duplicate stand-alone debugger as ultragdb

Duplicate stand-alone debugger project files to ultragdb, so can build ultragdb from command line.

```
pom.xml
		<profile>
			<id>build-standalone-debugger-rcp</id>
			<modules>
				<module>debug/org.eclipse.cdt.debug.application.product</module>
			</modules>
		</profile>
```


# Use of UltraGDB.isOn

通过下面的代码，截获函数的调用。
```
if (UltraGDB.isOn) {
	...
}
```


# Cygwin and MSYS2/MinGW


ToolSet 和 LaunchConfiguration的参数需要传递给ProcessFactory.exec()，这样才能知道是Cygwin还是MinGW.
也可以通过增加一个环境变量比如TOOLSET，在ProcessFactory.Builder.start()中，判断，并且相应的修正PATH。


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

```
Mintty works on all Windows versions from Windows XP onwards. Similarly to other Cygwin/MSYS terminals based on pseudo terminal ("pty") devices. 
Windows console input/output (as used by native Windows command-line programs) has interworking problems with "pty" mode (most notably character set, but also character-wise input and signal processing incompatibilities, see input/output interaction). 
Cygwin 3.1.0 compensates for this issue via the ConPTY API of Windows 10.
```

```
/mingw64/bin/gdb  是如何支持如下选项的？
--tty=TTY          Use TTY for input/output by the program being debugged.
```

研究cygwin1.dll中forkpty和openpty的实现。


将mintty konsole Eclipse CDT的修改合并到上流。

将存档的Cygwin和MSYS2上传到github上。

将mintty和konsole的修改upstream到上游。

研究gnome-terminal，研究如何使用openpty。

在KDE和GNOME的bug系统中fire一个bug，描述双击可执行文件需要在一个terminal emulator中运行这个程序。

```
./mintty/bin/mintty.exe --openpty &
/dev/pty1

echo hello >/dev/pty1

gdb --tty=/dev/pty1 emacs
run
```

# README.md

https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-readmes

If a repository contains more than one README file, then the file shown is chosen from locations in the following order: the .github directory, then the repository's root directory, and finally the docs directory.


# Run tests

If you want to run tests, do so as much as you like, enable github actions on your fork and then you can run all the tests you want.

