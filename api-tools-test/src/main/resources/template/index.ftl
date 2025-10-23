<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport">
    <meta content="ie=edge" http-equiv="X-UA-Compatible">
    <style>
        @font-face {
            font-family: 'mapway-font';
            font-style: normal;
            font-weight: 400;
            src: url(./webfonts/iconfont.woff2) format('woff2');
        }
        body {
            margin: 0px;
            padding: 0px;
            box-sizing: border-box;
        }

        #splash {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: start;
            width: 100%;
            height: 100vh;
            background: #000;
            position: relative;
        }

        #name {
            color: #fff;
            font-size: 2.5em;
            font-weight: bold;
            margin-bottom: 20px;
            margin-left: 250px;
        }

        #text {
            font-style: italic;
            color: white;
            margin-left: 250px;
            margin-bottom: 5px;
        }

        #bar {
            position: relative;
            width: 400px;
            height: 4px;
            background-color:#fff;
            margin-left: 250px;
            overflow: hidden;
        }
        #bar:before{
            content: "";
            position: absolute;
            width: 100px;
            height: 100%;
            top:0px;
            background-color: rgba(99, 157, 241, 0.63);
            animation: bar_frames 3s  infinite;
        }

        @keyframes bar_frames {
            0% {
                left:-100px;
            }
            50%{
                left:100%;
            }
            100%{
                left: -100px;
            }
        }
    </style>
    <title>DOC TEST</title>
</head>
<body>
<script>

</script>
<script >
   window.G_DATA_URL="http://localhost:7100/doc/data";
</script>
<script type="text/javascript" src="js/ace/ace.js"></script>
<script type="text/javascript" src="js/doc/docui.nocache.js"></script>
<iframe src="javascript:''"
        id="__gwt_historyFrame"
        style="position:absolute;width:0;height:0;border:0"></iframe>
</body>
</html>
