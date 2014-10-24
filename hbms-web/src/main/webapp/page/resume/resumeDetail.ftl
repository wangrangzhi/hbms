<table align="center" valign="top" cellpadding="0" cellspacing="0" border="0" width="100%">
  <tr>
    <td width="100%">
      <form id="resumeBasicInfoForm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="">
        <table border="0" align="center">
          <tr>
            <td>
              <table border="0" cellspacing="5px" cellpadding="2px" valign="middle">
                <tr>
                  <td align="right"><label for="originalResumeFile">原始简历:</label></td>
                  <td align="left" colspan="5">
                    <input class="easyui-filebox" name="originalResumeFile" data-options="width:400,buttonText:'选择简历',prompt:'请选择一份简历点击解析'"/>
                    <a href="#" class="easyui-linkbutton" iconCls="icon-save" type="save">解析</a>
                  </td>
                </tr>
                <tr>
                  <td align="right"><label for="name">姓名:</label></td>
                  <td align="left"><input class="easyui-textbox" name="name" data-options="required:true"/></td>
                  <td align="right"><label for="sex">性别:</label></td>
                  <td align="left">
                    <select id="sexOfCandidate" name="sex" class="easyui-combobox" data-options="required:true,panelWidth:50,panelHeight:60,editable:false">
                      <option type="radio" name="lang" value="男">男</option>
                      <option type="radio" name="lang" value="女">女</option>
                    </select>
                  </td>
                  <td align="right"><label for="age">年龄:</label></td>
                  <td align="left"><input class="easyui-textbox" name="age" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="education">教育背景:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="education" data-options="required:true"/></td>
                  <td align="right"><label for="marital">婚姻状况:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="marital" data-options="required:true"/></td>
                  <td align="right"><label for="location">居住地:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="location" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="telephone">电话:</label></td>
                  <td align="left"><input class="easyui-textbox" name="telephone" data-options="required:true"/></td>
                  <td align="right"><label for="email">邮箱:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="email" data-options="required:true"/></td>
                  <td align="right"><label for="workYears">工作年限:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="workYears" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="industry">行业:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="industry" data-options="required:true"/></td>
                  <td align="right"><label for="company">公司:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="company" data-options="required:true"/></td>
                  <td align="right"><label for="position">职位:</label></td>
                  <td align="left"><input class="easyui-textbox" type="text" name="position" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="salary">薪资:</label></td>
                  <td align="left" colspan="5"><input class="easyui-textbox" type="text" name="salary" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="selfEvaluation">个人评价:</label></td>
                  <td align="left" colspan="5"><input class="easyui-textbox" type="text" name="selfEvaluation" data-options="required:true,multiline:true,width:600,height:50"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="other">其他:</label></td>
                  <td align="left" colspan="5"><input class="easyui-textbox" type="text" name="other" data-options="required:true,multiline:true,width:600,height:50"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="keyword">搜索关键字:</label></td>
                  <td align="left"><input class="easyui-textbox" name="keyword" data-options="required:true"/></td>
                </tr>
                <tr>
                  <td align="right"><label for="language">语言:</label></td>
                  <td align="left" colspan="2"><input class="easyui-combobox" name="language" data-options="required:true,url:'/listItem/findListItemsOfCategoryByCode.do?code=Language',valueField:'value',textField:'value',editable:false"></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </form>
    </td>
  </tr>
</table>
<script>
  try {
    var row = $("#resumeDg").datagrid("getSelected");
    $('#resumeBasicInfoForm').form('load', row);
  } catch (t) {

  }
</script>