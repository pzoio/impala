<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>

<h1>Welcome to Impala</h1>
<p>If you can see this message, then you have successfully started up a minimal working multi-module application.</p>
<p>As the property <code>auto.reload.modules</code> is set true, any change you make to the application will
be automatically detected, and the affected modules will be reloaded.</p>

<p>You can test this for yourself be editing the following files.
<ul>
<li>Rename or change the <code>getMessage()</code> method in <code>MessageService</code>, in the <i>root</i> module. This will cause all the modules to reload.</li>
<li>Change the implementation class <code>MessageServiceImpl</code> class in the <i>implementation</i> module. This will cause just the implementation module to reload.</li>
<li>Make a change in <code>MessageController</code>, which will result in just the <i>web</i> module reloading.</li>
</ul>
</p>

<p>Notice how in this example, the jsp is hosted internally within the module, as is the style sheet 
(the same would apply for images).
</p>

<b>A message from message service:</b> <span style = "color:blue;"><%=request.getAttribute("message")%></span><br/>
</body>
</html>