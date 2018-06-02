# Simple Content Distribution over LAN

A set of apps containing a client, web server, content server, and several general DNS servers

Description:

  Client will connect to the web server for a web page
  Server will send a page to the client wtih links

  Client does not have any of the link urls cached 
    and will have to ask it's local DNS server which 
    will ask other DNS servers for the URL record
    local DNS will return the record to the client

  Client will now be able to ask the now cached content 
    server for the video or any other file
    
    
Usage:

  Run the .bat files associated wit each separate app
    located in root or their induvidual folder
    
  *aka. seversLaunch.bat and clientLaunch.bat
