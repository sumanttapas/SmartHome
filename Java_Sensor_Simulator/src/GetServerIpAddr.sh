#!/bin/sh

ifconfig | grep -A 1 wlan0 | grep inet | grep -Eo '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}'
