#!/bin/bash
args=("$@")
gradle_command=""
skip_tests=false
debug_mode=false

for ((i=0; i<"${#args[@]}"; i++)); do
    arg="${args[i]}"
    case "$arg" in
        "clean")
            gradle_command+="clean "
            ;;
        "build")
            gradle_command+="install "
            ;;
        "sonarqube")
            gradle_command+="sonar:sonar "
            ;;
        "test")
            gradle_command+="package -Dmaven.test.skip=false "
            ;;
        "-x")
            if [[ "${args[i+1]}" == "test" ]]; then
                skip_tests=true
                i=$((i+1))
            else
                echo "Opção -x requer algum argumento"
                exit 1
            fi
            ;;
        "-i")
            debug_mode=true
            ;;
        *)
            echo "Parâmetro não reconhecido: $arg"
            ;;
    esac
done

if [[ "$skip_tests" == true ]]; then
    gradle_command+="-Dmaven.test.skip=true "
fi

if [[ "$debug_mode" == true ]]; then
    gradle_command+="--debug "
fi

echo "install maven"
apt update
apt install maven -y
mvn -v

source dependencias.sh

echo "Executando: mvn $gradle_command"
mvn $gradle_command

cp target/pms-1.0-SNAPSHOT.war docker/pms.war
