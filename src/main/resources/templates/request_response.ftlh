<#-- @ftlvariable name="" type="io.ratesapi.demo.FreemarkerRequestAndResponse" -->
<html>
<head>
    <style>
        td {
            vertical-align: top;
            font-family: "courier new", courier, monospace;
            font-size: 14px;
            white-space: nowrap;
        }

        ul {
            padding: 0;
            margin: 0;
            float: left;
            list-style-type: none;
            display: list-item;
        }

        pre {
            font-family: "courier new", courier, monospace;
            padding: 0;
            margin: 0;
            float: left;
        }

        p {
            font-size: 16px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<p>REQUEST:</p>
<table>
    <tr>
        <td><b>URI:</b></td>
        <td>${requestUri}</td>
    </tr>
    <#if requestQueryParams?has_content>
        <tr>
            <td><b>Query params:</b></td>
            <td>
                <ul>
                    <#list requestQueryParams as key, value>
                        <li>${key} = ${value}</li>
                    </#list>
                </ul>
            </td>
        </tr>
    </#if>
    <#if requestFormParams?has_content>
        <tr>
            <td><b>Form params:</b></td>
            <td>
                <ul>
                    <#list requestFormParams as key, value>
                        <li>${key} = ${value}</li>
                    </#list>
                </ul>
            </td>
        </tr>
    </#if>
    <tr>
        <td><b>method:</b></td>
        <td>${requestMethod}</td>
    </tr>
    <#if requestContentType?has_content>
        <tr>
            <td><b>Content-Type:</b></td>
            <td>${requestContentType}</td>
        </tr>
    </#if>
    <tr>
        <td><b>Headers:</b></td>
        <td>
            <ul>
                <#list requestHeaders as header>
                    <li>${header.getName()} = ${header.getValue()}</li>
                </#list>
            </ul>
        </td>
    </tr>
    <#if requestBody?has_content>
        <tr>
            <td><b>Body:</b></td>
            <td>
                <pre>${requestBody}</pre>
            </td>
        </tr>
    </#if>
</table>
<p>RESPONSE:</p>
<table>
    <tr>
        <td><b>Status:</b></td>
        <td>${responseStatusLine}</td>
    </tr>
    <tr>
        <td><b>Content-Type:</b></td>
        <td>${responseContentType}</td>
    </tr>
    <tr>
        <td><b>Headers:</b></td>
        <td>
            <ul>
                <#list responseHeaders as header>
                    <li>${header.getName()} = ${header.getValue()}</li>
                </#list>
            </ul>
        </td>
    </tr>
    <#if responseBody?has_content>
        <tr>
            <td><b>Body:</b></td>
            <td>
                <pre>${responseBody?replace("<", "&lt")?replace(">", "&gt")}</pre>
            </td>
        </tr>
    </#if>
</table>
</body>
</html>
