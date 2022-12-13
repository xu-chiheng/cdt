# /********************************************************************************
#  * Copyright (c) 2022 徐持恒 Xu Chiheng
#  *
#  * This program and the accompanying materials are made available under the
#  * terms of the Eclipse Public License 2.0 which is available at
#  * http://www.eclipse.org/legal/epl-2.0.
#  *
#  * SPDX-License-Identifier: EPL-2.0
#  ********************************************************************************/


cd ..



PATCH_DIR="../cdt-patch"


CHANGED_FILES_OR_DIRS=(
    .github
    ultragdb
    releng
    .gitattributes
)

ADDED_FILES_OR_DIRS=(
    core/org.eclipse.cdt.core.native/src/org/eclipse/cdt/core/UltraGDB.java
    core/org.eclipse.cdt.core.native/src/org/eclipse/cdt/utils/pty/PTY2.java
    core/org.eclipse.cdt.core.native/src/org/eclipse/cdt/utils/pty/PTY2Util.java
    core/org.eclipse.cdt.core/utils/org/eclipse/cdt/internal/core/Cygwin1.java
    core/org.eclipse.cdt.core/utils/org/eclipse/cdt/internal/core/MSYS2.java
    remove-unneeded-plugins.cmd
    remove-unneeded-plugins.sh
    remove-unneeded-plugins.sh.sh
)


rm -rf "${PATCH_DIR}" \
&& mkdir -p "${PATCH_DIR}" \
&& git diff main...work -- "${CHANGED_FILES_OR_DIRS[@]}" > "${PATCH_DIR}/00.patch" \
&& git diff main...work -- "${ADDED_FILES_OR_DIRS[@]}" > "${PATCH_DIR}/01.patch" \



echo Completed!
read
