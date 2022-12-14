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

PATCH_DIR="$(cd ..; pwd)/cdt-patch"
FILE_LIST_DIR="$(pwd)/ultragdb"

# https://git-scm.com/docs/git-diff
# --diff-filter=[(A|C|D|M|R|T|U|X|B)…​[*]]
# Select only files that are Added (A), Copied (C), Deleted (D), Modified (M), Renamed (R), have their type (i.e. regular file, symlink, submodule, …​) changed (T), are Unmerged (U), are Unknown (X), or have had their pairing Broken (B). Any combination of the filter characters (including none) can be used. When * (All-or-none) is added to the combination, all paths are selected if there is any file that matches other criteria in the comparison; if there is no file that matches other criteria, nothing is selected.

# show all changed files
# git diff --name-only main...work

PATCH_00_FILES_OR_DIRS=(
    # show all changed non-java files
    $(git diff --name-only main...work | grep -v '\.java$' | tee "${FILE_LIST_DIR}/00.txt")
)

PATCH_01_FILES_OR_DIRS=(
    # show only added java files
    $(git diff --diff-filter=A --name-only main...work | grep '\.java$' | tee "${FILE_LIST_DIR}/01.txt")
)

PATCH_02_FILES_OR_DIRS=(
    # show only modified java files
    $(git diff --diff-filter=M --name-only main...work | grep '\.java$' | tee "${FILE_LIST_DIR}/02.txt")
)


rm -rf "${PATCH_DIR}"
mkdir -p "${PATCH_DIR}"
git diff main...work -- "${PATCH_00_FILES_OR_DIRS[@]}" > "${PATCH_DIR}/00.patch"
git diff main...work -- "${PATCH_01_FILES_OR_DIRS[@]}" > "${PATCH_DIR}/01.patch"
git diff main...work -- "${PATCH_02_FILES_OR_DIRS[@]}" > "${PATCH_DIR}/02.patch"



echo Completed!
read
