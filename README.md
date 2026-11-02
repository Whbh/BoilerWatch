# BoilerWatch

An automation tool that scrapes and then emails the most recent incidents from the West Lafayette Campus Daily Crime Log.

## Project Goal

The goal of this project is to automate the process of checking the West Lafayette Campus Daily Crime Log so users can just check their emails when the program has identified that there is new information. 

## MVP Scope

The core functionality of this project is to fetch the most recent crime log, extract the HTML, and print the logs for the user to read.

## Future Plans

Implement the ability to attempt to send and properly format an email at a regular interval as long as recent data is provided.

## Tech Stack

I will be writing the code in Java. I will use the Jakarta Mail, HttpClient, and Jsoup libraries. I plan to extract data from the Purdue crime logs by scraping their website. I also plan on storing this data on a txt or CSV file.
