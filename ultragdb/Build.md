
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

# Command Line(Apache Maven 3.8.6)

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
mvn package -DskipDoc=true -DskipTests=true -P build-ultragdb

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
```







