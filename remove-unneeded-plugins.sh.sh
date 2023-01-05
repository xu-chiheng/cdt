# /********************************************************************************
#  * Copyright (c) 2022 徐持恒 Xu Chiheng
#  *
#  * This program and the accompanying materials are made available under the
#  * terms of the Eclipse Public License 2.0 which is available at
#  * http://www.eclipse.org/legal/epl-2.0.
#  *
#  * SPDX-License-Identifier: EPL-2.0
#  ********************************************************************************/

#can't be executed directly
#!/bin/sh


cdt-plugins=(
    # how to get this list ?
    # Eclipse CDT -> Help -> About Eclipse IDE -> Installation Details -> Plug-ins
    # Click "Plug-in id" to sort by "Plug-in id" 

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
    org.eclipse.cdt.core.native
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
    org.eclipse.cdt.doc.user
    org.eclipse.cdt.docker.launcher
    org.eclipse.cdt.dsf
    org.eclipse.cdt.dsf.gdb
    org.eclipse.cdt.dsf.gdb.ui
    org.eclipse.cdt.dsf.ui
    org.eclipse.cdt.flatpak.launcher
    org.eclipse.cdt.gdb
    org.eclipse.cdt.gdb.ui
    org.eclipse.cdt.jsoncdb.core
    org.eclipse.cdt.jsoncdb.core.doc
    org.eclipse.cdt.jsoncdb.core.ui
    org.eclipse.cdt.launch
    org.eclipse.cdt.launch.remote
    org.eclipse.cdt.launch.serial.core
    org.eclipse.cdt.launch.serial.ui
    org.eclipse.cdt.make.core
    org.eclipse.cdt.make.ui
    org.eclipse.cdt.managedbuilder.core
    org.eclipse.cdt.managedbuilder.gnu.ui
    org.eclipse.cdt.managedbuilder.headlessbuilderapp
    org.eclipse.cdt.managedbuilder.ui
    org.eclipse.cdt.meson.core
    org.eclipse.cdt.meson.docs
    org.eclipse.cdt.meson.ui
    org.eclipse.cdt.meson.ui.editor
    org.eclipse.cdt.native.serial
    org.eclipse.cdt.platform.branding
    org.eclipse.cdt.remote.core
    org.eclipse.cdt.target
    org.eclipse.cdt.testsrunner
    org.eclipse.cdt.testsrunner.boost
    org.eclipse.cdt.testsrunner.gtest
    org.eclipse.cdt.testsrunner.qttest
    org.eclipse.cdt.testsrunner.tap
    org.eclipse.cdt.ui

    org.eclipse.launchbar
    org.eclipse.launchbar.core
    org.eclipse.launchbar.remote
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
    org.eclipse.remote.proxy.ui
    org.eclipse.remote.serial.core
    org.eclipse.remote.serial.ui
    org.eclipse.remote.telnet.core
    org.eclipse.remote.telnet.ui
    org.eclipse.remote.ui

    org.eclipse.tools.templates.core
    org.eclipse.tools.templates.freemarker
    org.eclipse.tools.templates.ui

)

ultragdb-plugins=("${cdt-plugins[@]}")



case $(uname -o) in
    Cygwin )
        platform-plugins=(
            org.eclipse.cdt.core.win32
            org.eclipse.cdt.core.win32.x86_64
        )
        ;;
    Linux )
        platform-plugins=(
            org.eclipse.cdt.core.linux
            org.eclipse.cdt.core.linux.x86_64
        )
        ;;
    * )
        echo "Unknown OS"
        exit 1
        ;;

esac

cdt-plugins+=("${platform-plugins[@]}")
ultragdb-plugins+=("${platform-plugins[@]}")


for plugin_dir in $(find . -mindepth 2 -maxdepth 2 -type d ! -wholename '*/.git/*' -a ! -wholename '*/~git-tools~/*')
do
    # echo $plugin_dir
    # https://stackoverflow.com/questions/3685970/check-if-a-bash-array-contains-a-value
    # https://www.folkstalk.com/2022/09/check-if-a-bash-array-contains-a-value-with-code-examples.html
    if [[ ! " ${cdt-plugins[*]} " =~ " $(basename "${plugin_dir}") " ]]; then
        echo $plugin_dir
        rm -rf $plugin_dir
     fi
done


echo Completed!
read
