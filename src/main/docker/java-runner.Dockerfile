FROM eclipse-temurin:21-jdk-alpine
WORKDIR /code
ENV JAVA_TOOL_OPTIONS="-Xms64m -Xmx256m"
CMD sh -c "javac Main.java 2> compile.err || exit 100; \
           /usr/bin/time -f '%M' java Main < input.txt"
