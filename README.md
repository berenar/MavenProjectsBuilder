<p align="center">
  <img src="https://github.com/berenar/mvnCompiler/blob/master/src/main/resources/mvn_logo_2.png"/>
</p>

# mvnCompiler
Java app for compiling numerous mvn projects at once in a specified order.

## Install
### Windows
No install needed, just execute the *.exe* or *.jar* in *MvnCompiler/target* folder.

## Configure
No configuration needed.

## Use
Projects are introduced in each row.
* Add as many projects as needed with the "+" button.
* To select a local project just input it's path or click the **Local** button.
* To select a remote project just input it's URL and clicl the **Clone** button to clone it to a local folder.
* **Compile all** button compiles all introduced projects in order.
* **Export** button creates a file with all project paths in the app (one per line).
* **Import** button opens a file of paths (one per line) and inserts them in the app.
* **Reset** button resets the app and tries to delete the temporary folder (*/.mvnCompiler_temp*) with the cloned remote projects.



<p align="center">
  <br>
<img src="https://github.com/berenar/mvnCompiler/blob/master/mvncompiler_screenshot.png"/>
</p>
