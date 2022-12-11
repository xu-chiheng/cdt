
cd "$(dirname "$0")"

nohup konsole --nofork --workdir="$(pwd)" -e bash -i remove-unneeded-plugins.sh.sh
