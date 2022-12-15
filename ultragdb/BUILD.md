
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


```bash
# command line build :
mvn clean
mvn package -DskipDoc=true -DskipTests=true

# output file :
# releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip
# output directory :
# releng/org.eclipse.cdt.repo/target/repository
```

```bash
# Running inside the target application
# d:/builder/ must first contain an eclipse product
d:/builder/eclipsec.exe \
    -application org.eclipse.equinox.p2.director \
    -repository "jar:file:$(cygpath -m "$(pwd)")/releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip!" \
    -installIU org.eclipse.cdt.feature.group

```
```bash
# Installing / uninstalling IUs into a target product
# d:/ultragdb/ must first contain an eclipse product
d:/builder/eclipsec.exe \
    -application org.eclipse.equinox.p2.director \
    -repository "jar:file:$(cygpath -m "$(pwd)")/releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip!" \
    -installIU org.eclipse.cdt.feature.group \
    -tag AddCDT \
    -destination d:/ultragdb/ \
    -profile SDKProfile
```
```bash
# Installing a complete product
# d:/ultragdb/ must first contain an eclipse product
d:/builder/eclipsec.exe \
    -application org.eclipse.equinox.p2.director \
    -repository "jar:file:$(cygpath -m "$(pwd)")/releng/org.eclipse.cdt.repo/target/org.eclipse.cdt.repo.zip!" \
    -installIU org.eclipse.cdt.feature.group \
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

