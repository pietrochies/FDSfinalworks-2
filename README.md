Os endpoints funcionam ao serem chamados no terminal, mas ainda não esta fuincionando com o cliente Rest.
codespaces conta com problema de build e esta usando java 11 por nenhum motivo aparente, usar: export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
java -version
para rodar o projeto em 21
