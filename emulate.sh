#!/bin/bash

arm-linux-gnueabi-gcc -o test-out -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $1
qemu-arm -L /usr/arm-linux-gnueabi/ test-out
code=$?
rm -rf ./test-out
exit $code