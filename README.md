COMPILATION & EXECUTION:

1) Requirements:
  a) Browser: Mozilla Firefox
  b) Java Compiler: Java Development Kit (JDK)
    -To check if it is already installed:
      i) Open the terminal
      ii) Type in the command "javac -version"
      iii) If it is installed, you will get the version number
      iv) if not, you will need to install it.
        -You may find this link helpful: https://appuals.com/fix-javac-is-not-recognized-on-windows-10/

2) Compilation:
  a) Open the terminal and change the path directory to the project folder
  b) Type the Command "javac -deprecation *.java" to compile all the files
    -There will be several warnings, but they will NOT prevent the execution

3) Execution:
  A) Browser Set-up:
    a) Open the Firefox browser, and go to the Settings/Options
    b) Find the Network Settings (usually within the General Tab) and click "Settings"
    c) Under "Configure Proxy Access to the Internet" click the "Manual Proxy Configuration"
    d) Set the "HTTP Proxy" to "localhost" with a port number between 1 and 65536
    e) Check the box directly below saying "Use this proxy server for all protocols"
    f) Click the 'OK' button
  
  B) Run the Program:
    a) After the Browser has been set up, open a new tab in Firefox
    b) Open the terminal and redirect to the project folder directory
    c) 
	) In the Browser put http://gaia.cs.umass.edu/ in the IP/DNS address bar

    When the proxy gets a request:
    	it checks if the requested object is cached
    		if YES, the proxy: 
    			1. will return the object from the cache
    				// without contacting the server
    		If NO, (the object is not )cached, the proxy:
    			1. retrieves the object from the server
    			2. returns the object to the client
    			3. caches a copy for future requests