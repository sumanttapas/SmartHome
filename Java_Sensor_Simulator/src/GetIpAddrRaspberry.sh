#!/bin/sh

#nmap -sP 192.168.1.0/24 > text.txt
arp -a | grep b8:27:eb | grep -Eo '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}' 
