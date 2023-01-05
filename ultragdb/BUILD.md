
# Eclipse Committers 2022-12

```
Double click "remove-unneeded-plugins.cmd" to remove unneeded plugins.

Eclipse Committers
Import -> Git -> Projects from Git, select the source directory, like “E:\Source\IDE\cdt”.

Run "git reset --hard HEAD" to restore deleted files

Eclipse Committers
In Java Perspective, double click "org.eclipse.cdt.target/cdt.target"
Or in Plug-in Development Perspective, double click 
"org.eclipse.cdt.root/releng/org.eclipse.cdt.target/cdt.target"

Target Definition -> Set As Active Target Platform
Right down corner : Resolving "cdt" target definition ... 
Double click it to show progress.
Wait it to complete.

org.eclipse.cdt.ui  Run/Debug as Eclipse Application

```

# Command Line

```bash
# command line build :
mvn clean
mvn package -DskipDoc=true -DskipTests=true

# output file :
# releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip
# output directory :
# releng/org.eclipse.cdt.repo/target/repository


# https://github.com/eclipse-cdt/cdt/blob/main/StandaloneDebugger.md
# https://www.infoq.com/presentations/best-practices-cdt-debugger/
mvn package -DskipDoc=true -DskipTests=true -P build-standalone-debugger-rcp

find . -type f -name org.eclipse.cdt.repo.zip
# ./releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip

find . -type f -name 'cdt-stand-alone-debugger-*'
# ./debug/org.eclipse.cdt.debug.application.product/target/products/cdt-stand-alone-debugger-11.1.0-20221216-0554-linux.gtk.aarch64.tar.gz
# ./debug/org.eclipse.cdt.debug.application.product/target/products/cdt-stand-alone-debugger-11.1.0-20221216-0554-linux.gtk.x86_64.tar.gz
# ./debug/org.eclipse.cdt.debug.application.product/target/products/cdt-stand-alone-debugger-11.1.0-20221216-0554-macosx.cocoa.x86_64.tar.gz
# ./debug/org.eclipse.cdt.debug.application.product/target/products/cdt-stand-alone-debugger-11.1.0-20221216-0554-win32.win32.x86_64.zip

find . -type d -name 'cdt-stand-alone-debugger' -o -name 'cdt-stand-alone-debugger.app'
# ./debug/org.eclipse.cdt.debug.application.product/target/products/org.eclipse.cdt.debug.application.product/linux/gtk/aarch64/cdt-stand-alone-debugger
# ./debug/org.eclipse.cdt.debug.application.product/target/products/org.eclipse.cdt.debug.application.product/linux/gtk/x86_64/cdt-stand-alone-debugger
# ./debug/org.eclipse.cdt.debug.application.product/target/products/org.eclipse.cdt.debug.application.product/macosx/cocoa/x86_64/cdt-stand-alone-debugger.app
# ./debug/org.eclipse.cdt.debug.application.product/target/products/org.eclipse.cdt.debug.application.product/win32/win32/x86_64/cdt-stand-alone-debugger


# mvn package -DskipDoc=true -DskipTests=true -P defaultCdtTarget
# [INFO] ------------------------------------------------------------------------
# [INFO] BUILD SUCCESS
# [INFO] ------------------------------------------------------------------------
# [INFO] Total time:  08:50 min
# [INFO] Finished at: 2022-12-16T20:25:26+08:00
# [INFO] ------------------------------------------------------------------------


# mvn package -DskipDoc=true -DskipTests=true -P production
# [INFO] ------------------------------------------------------------------------
# [ERROR] Failed to execute goal org.apache.maven.plugins:maven-antrun-plugin:3.0.0:run (natives) on project org.eclipse.cdt.core.native: An Ant BuildException has occured: exec returned: 2
# [ERROR] around Ant part ...<exec newenvironment="false" dir="./native_src" failOnError="true" executable="make">... @ 4:90 in C:\Users\Administrator\IDE\cdt\core\org.eclipse.cdt.core.native\target\antrun\build-main.xml
# [ERROR] -> [Help 1]
# [ERROR]
# [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
# [ERROR] Re-run Maven using the -X switch to enable full debug logging.
# [ERROR]
# [ERROR] For more information about the errors and possible solutions, please read the following articles:
# [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
# [ERROR]
# [ERROR] After correcting the problems, you can resume the build with the command
# [ERROR]   mvn <args> -rf :org.eclipse.cdt.core.native


builder_eclipse() {
    local output_file="releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip"
    if [ -d .git ] && [ -f "${output_file}" ]; then
        # d:/builder/ must first contain an eclipse product, like Eclipse SDK
        d:/builder/eclipsec.exe \
            -application org.eclipse.equinox.p2.director \
            -repository "jar:file:$(cygpath -m "$(pwd)")/${output_file}!" \
            "$@"
    else
        false
    fi
}

# Running inside the target application
builder_eclipse -installIU org.eclipse.cdt.feature.group

# https://stackoverflow.com/questions/1527049/how-can-i-join-elements-of-an-array-in-bash
function join_by {
  local d=${1-} f=${2-}
  if shift 2; then
    printf %s "$f" "${@/#/$d}"
  fi
}

# builder_eclipse -list
FEATURE_GROUPS=(
    $(builder_eclipse -list 2>/dev/null | cut -d '=' -f 1 | grep '.feature.group')
)
PLUG_INS=(
    $(builder_eclipse -list 2>/dev/null | cut -d '=' -f 1 | grep -v '.feature.group'  | grep -v '.feature' | grep -v '.source')
)

FEATURE_GROUPS=(
    org.eclipse.cdt.autotools.feature.group
    org.eclipse.cdt.autotools.source.feature.group
    org.eclipse.cdt.build.crossgcc.feature.group
    org.eclipse.cdt.build.crossgcc.source.feature.group
    org.eclipse.cdt.cmake.feature.group
    org.eclipse.cdt.cmake.source.feature.group
    org.eclipse.cdt.core.autotools.feature.group
    org.eclipse.cdt.core.autotools.source.feature.group
    org.eclipse.cdt.debug.gdbjtag.feature.group
    org.eclipse.cdt.debug.gdbjtag.source.feature.group
    org.eclipse.cdt.debug.standalone.feature.group
    org.eclipse.cdt.debug.standalone.source.feature.group
    org.eclipse.cdt.debug.ui.memory.feature.group
    org.eclipse.cdt.debug.ui.memory.source.feature.group
    org.eclipse.cdt.docker.launcher.feature.group
    org.eclipse.cdt.docker.launcher.source.feature.group
    org.eclipse.cdt.examples.dsf.feature.group
    org.eclipse.cdt.examples.dsf.source.feature.group
    org.eclipse.cdt.feature.group
    org.eclipse.cdt.gdb.feature.group
    org.eclipse.cdt.gdb.source.feature.group
    org.eclipse.cdt.gnu.build.feature.group
    org.eclipse.cdt.gnu.build.source.feature.group
    org.eclipse.cdt.gnu.debug.feature.group
    org.eclipse.cdt.gnu.debug.source.feature.group
    org.eclipse.cdt.gnu.dsf.feature.group
    org.eclipse.cdt.gnu.dsf.source.feature.group
    org.eclipse.cdt.gnu.multicorevisualizer.feature.group
    org.eclipse.cdt.gnu.multicorevisualizer.source.feature.group
    org.eclipse.cdt.launch.remote.feature.group
    org.eclipse.cdt.launch.remote.source.feature.group
    org.eclipse.cdt.launch.serial.feature.feature.group
    org.eclipse.cdt.launch.serial.feature.source.feature.group
    org.eclipse.cdt.llvm.dsf.lldb.feature.group
    org.eclipse.cdt.llvm.dsf.lldb.source.feature.group
    org.eclipse.cdt.managedbuilder.llvm.feature.group
    org.eclipse.cdt.managedbuilder.llvm.source.feature.group
    org.eclipse.cdt.meson.feature.group
    org.eclipse.cdt.meson.source.feature.group
    org.eclipse.cdt.msw.feature.group
    org.eclipse.cdt.msw.source.feature.group
    org.eclipse.cdt.native.feature.group
    org.eclipse.cdt.native.source.feature.group
    org.eclipse.cdt.platform.feature.group
    org.eclipse.cdt.platform.source.feature.group
    org.eclipse.cdt.sdk.feature.group
    org.eclipse.cdt.source.feature.group
    org.eclipse.cdt.testsrunner.feature.feature.group
    org.eclipse.cdt.testsrunner.feature.source.feature.group
    org.eclipse.cdt.unittest.feature.feature.group
    org.eclipse.cdt.unittest.feature.source.feature.group
    org.eclipse.cdt.visualizer.feature.group
    org.eclipse.cdt.visualizer.source.feature.group
    org.eclipse.launchbar.feature.group
    org.eclipse.launchbar.remote.feature.group
    org.eclipse.launchbar.remote.source.feature.group
    org.eclipse.launchbar.source.feature.group
    org.eclipse.remote.console.feature.group
    org.eclipse.remote.console.source.feature.group
    org.eclipse.remote.feature.group
    org.eclipse.remote.proxy.feature.group
    org.eclipse.remote.proxy.source.feature.group
    org.eclipse.remote.serial.feature.group
    org.eclipse.remote.serial.source.feature.group
    org.eclipse.remote.source.feature.group
    org.eclipse.remote.telnet.feature.group
    org.eclipse.remote.telnet.source.feature.group
    org.eclipse.tm.terminal.connector.cdtserial.feature.feature.group
    org.eclipse.tm.terminal.connector.cdtserial.feature.source.feature.group
    org.eclipse.tm.terminal.connector.local.feature.feature.group
    org.eclipse.tm.terminal.connector.local.feature.source.feature.group
    org.eclipse.tm.terminal.connector.remote.feature.feature.group
    org.eclipse.tm.terminal.connector.remote.feature.source.feature.group
    org.eclipse.tm.terminal.connector.ssh.feature.feature.group
    org.eclipse.tm.terminal.connector.ssh.feature.source.feature.group
    org.eclipse.tm.terminal.connector.telnet.feature.feature.group
    org.eclipse.tm.terminal.connector.telnet.feature.source.feature.group
    org.eclipse.tm.terminal.control.feature.feature.group
    org.eclipse.tm.terminal.control.feature.source.feature.group
    org.eclipse.tm.terminal.feature.feature.group
    org.eclipse.tm.terminal.feature.source.feature.group
    org.eclipse.tm.terminal.view.feature.feature.group
    org.eclipse.tm.terminal.view.feature.source.feature.group
)

PLUG_INS=(
    202212150823.Default
    202212150823.extra
    202212150823.extra-jsoncdb
    202212150823.launchbar
    202212150823.main
    202212150823.remote
    202212150823.remote_sdk
    202212150823.terminal_main
    202212150823.terminal_sdk
    a.jre.javase
    a.jre.javase
    a.jre.javase
    com.google.gson
    com.sun.jna
    com.sun.jna.platform
    com.sun.xml.bind
    jakarta.xml.bind
    javax.activation
    javax.xml
    javax.xml.stream
    org.apache.commons.io
    org.eclipse.cdt
    org.eclipse.cdt.autotools.core
    org.eclipse.cdt.autotools.docs
    org.eclipse.cdt.autotools.ui
    org.eclipse.cdt.build.crossgcc
    org.eclipse.cdt.build.gcc.core
    org.eclipse.cdt.build.gcc.ui
    org.eclipse.cdt.cmake.core
    org.eclipse.cdt.cmake.ui
    org.eclipse.cdt.codan.checkers
    org.eclipse.cdt.codan.checkers.ui
    org.eclipse.cdt.codan.core
    org.eclipse.cdt.codan.core.cxx
    org.eclipse.cdt.codan.ui
    org.eclipse.cdt.codan.ui.cxx
    org.eclipse.cdt.core
    org.eclipse.cdt.core.autotools.core
    org.eclipse.cdt.core.autotools.ui
    org.eclipse.cdt.core.linux
    org.eclipse.cdt.core.linux.aarch64
    org.eclipse.cdt.core.linux.ppc64le
    org.eclipse.cdt.core.linux.x86_64
    org.eclipse.cdt.core.macosx
    org.eclipse.cdt.core.native
    org.eclipse.cdt.core.win32
    org.eclipse.cdt.core.win32.x86_64
    org.eclipse.cdt.debug.application
    org.eclipse.cdt.debug.application.doc
    org.eclipse.cdt.debug.core
    org.eclipse.cdt.debug.core.memory
    org.eclipse.cdt.debug.gdbjtag
    org.eclipse.cdt.debug.gdbjtag.core
    org.eclipse.cdt.debug.gdbjtag.ui
    org.eclipse.cdt.debug.ui
    org.eclipse.cdt.debug.ui.memory.floatingpoint
    org.eclipse.cdt.debug.ui.memory.memorybrowser
    org.eclipse.cdt.debug.ui.memory.search
    org.eclipse.cdt.debug.ui.memory.traditional
    org.eclipse.cdt.debug.ui.memory.transport
    org.eclipse.cdt.doc.isv
    org.eclipse.cdt.doc.user
    org.eclipse.cdt.docker.launcher
    org.eclipse.cdt.dsf
    org.eclipse.cdt.dsf.gdb
    org.eclipse.cdt.dsf.gdb.multicorevisualizer.ui
    org.eclipse.cdt.dsf.gdb.ui
    org.eclipse.cdt.dsf.ui
    org.eclipse.cdt.examples.dsf
    org.eclipse.cdt.examples.dsf.pda
    org.eclipse.cdt.examples.dsf.pda.ui
    org.eclipse.cdt.flatpak.launcher
    org.eclipse.cdt.gdb
    org.eclipse.cdt.gdb.ui
    org.eclipse.cdt.jsoncdb.arm
    org.eclipse.cdt.jsoncdb.core
    org.eclipse.cdt.jsoncdb.core.doc
    org.eclipse.cdt.jsoncdb.core.ui
    org.eclipse.cdt.jsoncdb.hpenonstop
    org.eclipse.cdt.jsoncdb.intel
    org.eclipse.cdt.jsoncdb.microsoft
    org.eclipse.cdt.jsoncdb.nvidia
    org.eclipse.cdt.launch
    org.eclipse.cdt.launch.remote
    org.eclipse.cdt.launch.serial.core
    org.eclipse.cdt.launch.serial.ui
    org.eclipse.cdt.llvm.dsf.lldb.core
    org.eclipse.cdt.llvm.dsf.lldb.ui
    org.eclipse.cdt.make.core
    org.eclipse.cdt.make.ui
    org.eclipse.cdt.managedbuilder.core
    org.eclipse.cdt.managedbuilder.gnu.ui
    org.eclipse.cdt.managedbuilder.headlessbuilderapp
    org.eclipse.cdt.managedbuilder.llvm.ui
    org.eclipse.cdt.managedbuilder.ui
    org.eclipse.cdt.meson.core
    org.eclipse.cdt.meson.docs
    org.eclipse.cdt.meson.ui
    org.eclipse.cdt.meson.ui.editor
    org.eclipse.cdt.msw.build
    org.eclipse.cdt.native.serial
    org.eclipse.cdt.platform.branding
    org.eclipse.cdt.remote.core
    org.eclipse.cdt.sdk
    org.eclipse.cdt.testsrunner
    org.eclipse.cdt.testsrunner.boost
    org.eclipse.cdt.testsrunner.gtest
    org.eclipse.cdt.testsrunner.qttest
    org.eclipse.cdt.testsrunner.tap
    org.eclipse.cdt.ui
    org.eclipse.cdt.unittest
    org.eclipse.cdt.util
    org.eclipse.cdt.visualizer.core
    org.eclipse.cdt.visualizer.ui
    org.eclipse.cdt_root
    org.eclipse.launchbar.core
    org.eclipse.launchbar.remote.core
    org.eclipse.launchbar.remote.ui
    org.eclipse.launchbar.ui
    org.eclipse.launchbar.ui.controls
    org.eclipse.remote.console
    org.eclipse.remote.core
    org.eclipse.remote.doc.isv
    org.eclipse.remote.jsch.core
    org.eclipse.remote.jsch.ui
    org.eclipse.remote.proxy.core
    org.eclipse.remote.proxy.protocol.core
    org.eclipse.remote.proxy.server.core
    org.eclipse.remote.proxy.server.linux.ppc64le
    org.eclipse.remote.proxy.server.linux.x86_64
    org.eclipse.remote.proxy.server.macosx.x86_64
    org.eclipse.remote.proxy.ui
    org.eclipse.remote.serial.core
    org.eclipse.remote.serial.ui
    org.eclipse.remote.telnet.core
    org.eclipse.remote.telnet.ui
    org.eclipse.remote.ui
    org.eclipse.tm.terminal.connector.cdtserial
    org.eclipse.tm.terminal.connector.local
    org.eclipse.tm.terminal.connector.process
    org.eclipse.tm.terminal.connector.remote
    org.eclipse.tm.terminal.connector.ssh
    org.eclipse.tm.terminal.connector.telnet
    org.eclipse.tm.terminal.control
    org.eclipse.tm.terminal.view.core
    org.eclipse.tm.terminal.view.ui
    org.eclipse.tools.templates.core
    org.eclipse.tools.templates.freemarker
    org.eclipse.tools.templates.ui
    org.freemarker
    org.yaml.snakeyaml
)


builder_eclipse -installIU  "$(join_by , "${FEATURE_GROUPS[@]}")"




# Installing / uninstalling IUs into a target product
# d:/ultragdb/ must first contain an eclipse product
builder_eclipse -installIU org.eclipse.cdt.feature.group \
    -tag AddCDT \
    -destination d:/ultragdb/ \
    -profile SDKProfile

# Installing a complete product
# d:/ultragdb/ must first contain an eclipse product
builder_eclipse -installIU org.eclipse.cdt.feature.group \
    -tag InitialState \
    -destination d:/ultragdb/ \
    -profile SDKProfile \
    -profileProperties org.eclipse.update.install.features=true \
    -bundlepool d:/ultragdb/ \
    -p2.os win32 \
    -p2.ws win32 \
    -p2.arch x86_64 \
    -roaming
```





Google "eclipse package product from p2 repository"

https://www.google.com/search?q=eclipse+package+product+from+p2+repository

Building p2 Repositories and Products

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Ftasks%2Fpde_p2_builds.htm

Installing software using the p2 director application

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fp2_director.html

Product Builds with p2

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Ftasks%2Fpde_p2_productbuilds.htm

Tycho/eclipse-repository

https://wiki.eclipse.org/Tycho/eclipse-repository


Tycho p2 Director Plugin          tycho-p2-director:archive-products

https://tycho.eclipseprojects.io/doc/latest/tycho-p2/tycho-p2-director-plugin/archive-products-mojo.html


Tycho/Split eclipse repository and product packaging types

https://wiki.eclipse.org/Tycho/Split_eclipse_repository_and_product_packaging_types

Using org.eclipse.equinox.p2.director.app.application to install OEPE

https://community.oracle.com/tech/developers/discussion/2513891/using-org-eclipse-equinox-p2-director-app-application-to-install-oepe





Google "eclipse export product command line"

https://www.google.com/search?q=eclipse+export+product+command+line&oq=eclipse+product+command+line

Product Export

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Fguide%2Ftools%2Fexport_wizards%2Fexport_product.htm

Product Configuration

https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Fguide%2Ftools%2Ffile_wizards%2Fnew_product_config.htm

Eclipse product and application deployment - Tutorial

https://www.vogella.com/tutorials/EclipseProductDeployment/article.html

Eclipse RCP (Rich Client Platform) - Tutorial

https://www.vogella.com/tutorials/EclipseRCP/article.html







Maven Tycho for building Eclipse plug-ins, OSGi bundles and Eclipse applications with the command line - Tutorial

https://www.vogella.com/tutorials/EclipseTycho/article.html


Building a “headless RCP” application with Tycho

http://blog.vogella.com/2020/01/20/building-a-headless-rcp-application-with-tycho/








