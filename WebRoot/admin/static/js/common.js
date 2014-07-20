//删除操作
function check() {
    var sucin = 0;
    $("input[name='id[]']:checked").each(function (i, n) {
        sucin = 1;
    });
    if (sucin == 0) {
        alert("请选择要删除的ID");
        return false;
    } else {
        return confirm('确认删除？');
        return true;
    }
}

/**
 * 通用AJAX提交
 * @param  {string} url    表单提交地址
 * @param  {string} formObj    待提交的表单对象或ID
 */
function ajaxSubmit(url, formObj, validate) {
    if (!formObj || formObj == '') {
        var formObj = 'form';
    }
    if (!url || url == '') {
        var url = document.URL;
    }

    $(formObj).submit(function () {

        $(this).ajaxSubmit({
            type: "post",
            url: url,
            beforeSubmit: validate,
            success: submitSuccess
        });

        //此处必须返回false，阻止常规的form提交
        return false;
    });

}

/**
 * 表单提交成功后的反馈
 * @param {type} data
 * @returns {undefined}
 */
function submitSuccess(data) {
    var title = null;
    var type = null;
    if (data.status == 1) {
        title = "操作成功";
        type = BootstrapDialog.TYPE_PRIMARY;
    } else {
        title = "操作失败";
        type = BootstrapDialog.TYPE_WARNING;
    }

    BootstrapDialog.show({
        title: title,
        message: data.info,
        type: type,
        buttons: [
            {
                label: '确定',
                cssClass: 'btn-primary',
                action: function (dialog) {
                    window.location.href = data.url;
                    dialog.close();
                }
            }
        ]
    });

    if (data.url && data.url != '') {
        setTimeout(function () {
            top.window.location.href = data.url;
        }, 3000);
    }

    if (data.url == '') {
        setTimeout(function () {
            top.window.location.reload();
        }, 2000);
    }
}


/**
 * 检测字符串是否是电子邮件地址格式
 * @param  {string} value    待检测字符串
 */
function isEmail(value) {
    var Reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return Reg.test(value);
}

function clickCheckbox() {
    $(".chooseAll").click(function () {
        var status = $(this).prop('checked');
        $("tbody input[type='checkbox']").prop("checked", status);
        $(".chooseAll").prop("checked", status);
        $(".unsetAll").prop("checked", false);
    });
    $(".unsetAll").click(function () {
        var status = $(this).prop('checked');
        $("tbody input[type='checkbox']").each(function () {
            $(this).prop("checked", !$(this).prop("checked"));
        });
        $(".unsetAll").prop("checked", status);
        $(".chooseAll").prop("checked", false);
    });
}

/**
 * 图片上传之前进行预览
 * @param dom
 * @param previewId
 */
function preview_image(dom, previewId) {

    var pic = document.getElementById(previewId);
    var file = dom;

    var ext = file.value.substring(file.value.lastIndexOf(".") + 1).toLowerCase();

    // gif在IE浏览器暂时无法显示
    if (ext != 'png' && ext != 'jpg' && ext != 'jpeg') {
        alert("图片的格式必须为png或者jpg或者jpeg格式！");
        return;
    }

    var isIE = navigator.userAgent.match(/MSIE/) != null;
    var isIE6 = navigator.userAgent.match(/MSIE 6.0/) != null;

    if (isIE) {
        file.select();
        var reallocalpath = document.selection.createRange().text;

        // IE6浏览器设置img的src为本地路径可以直接显示图片
        if (isIE6) {
            pic.src = reallocalpath;
        } else {
            // 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
            pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
            // 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
            pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
        }

    } else {

        var file = file.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function(e){
            var pic = document.getElementById(previewId);
            pic.src=this.result;
        }

    }
}


