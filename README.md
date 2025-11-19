
codespaces conta com problema de build e esta usando java 11 por nenhum motivo aparente, 
usar: 
sudo apt update
sudo apt install -y openjdk-21-jdk
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

para rodar o projeto em 21
