FROM gcc:13.2.0
RUN apt-get update && \
    apt-get install -y time coreutils && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /code

CMD sh -c "\
ulimit -s 8192 && \
g++ main.cpp -O2 -std=gnu++17 -o main 2> compile.err || exit 100; \
timeout 2s /usr/bin/time -f '%M' ./main < input.txt \
"
