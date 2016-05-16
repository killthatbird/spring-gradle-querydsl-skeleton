var submitValidateCheck = 0;
(function ($) {
	// *******************
	// placeholderのタイトル設定
	// *******************
	$('.text').blur(function() {
		var $$ = $(this);
		if ($$.val() == '' || $$.val() == $$.attr('data-placeholder')) {
			$$.css('color', '#999999').val($$.attr('data-placeholder'));
		}
	}).focus(function() {
		submitValidateCheck = 0;
		var $$ = $(this);
		$$.css('color', '#000000');
		if ($$.val() == $$.attr('data-placeholder')) {
			$$.val('');
		}
	}).parents('form:first').submit(function() {
		submitValidateCheck = 1;
		
		//validatorチェック時にplaceholderを削除するため
		$('.text').each(function() {
			if ($(this).val() == $(this).attr('data-placeholder')) {
				//$$.triggerHandler('focus');
				$(this).val('');
			}
		});
	}).end().blur();
	
}(jQuery));

// ***************************
//  Validator 初期化処理
// ***************************
(function ($) {
    "use strict";
    // Validatorの初期値を変更します
    $.validator.setDefaults({
        // NG項目のclass
        errorClass: 'has-error',
        // OK項目のclass
        validClass: 'has-success',
        // 入力チェックNGの場合、項目のform-groupにerrorClassを設定します
        highlight: function (element, errorClass, validClass) {
            $(element).closest('.form-group').addClass(errorClass).removeClass(validClass);
        },
        // 入力チェックOKの場合、項目のform-groupにvalidClassを設定します
        unhighlight: function (element, errorClass, validClass) {
            $(element).closest('.form-group').removeClass(errorClass).addClass(validClass);
        }
    });
}(jQuery));

/**
 * jQueryのValidateのGroupを
 * data-validate-groupタグから取得する
 */
var getDataValidateGroups = function() {
	var result = {};
	$("input[data-validate-group]").each(function(i) {
		var groupName = $(this).attr("data-validate-group");
		if (groupName != undefined) {
			result[groupName] = (result[groupName] != undefined ? result[groupName] + " " : "") + $(this).attr("name");
		}
	});
	return result;
}

// DOM構築後の処理
$(function () {
    "use strict";
    // ページ内のすべてのフォームにValidatorを設定します
    $('form').each(function (index, element) {
        var $form = $(element);
        var validator = $form.validate({
        	ignore: [],
        	errorPlacement: function(error,element){
        		
        		//エラーの表示場所を指定したい場合
        		var customPlace = document.getElementById(element.attr("name") + "_javascript_validate");
        		if (customPlace) {
        			error.insertAfter(customPlace);
        		} else {
        			error.insertAfter(element);
        		}
        		
        	},
        	showErrors: function(errorMap, errorList) {
        		$(".messagebox").show();
                this.defaultShowErrors();
                if (submitValidateCheck == 1) {
                	//submitのvalidateチェックの場合
                	//エラーメッセージ表示後　data-placeholder を value に戻す
                	$('.text').each(function() {
            			var $$ = $(this);
            			if ($$.val() == '') {
            				$$.val($$.attr('data-placeholder'));
            			}
            		});
                	
                }        		
        	},
        	submitHandler: function(form) {
        		//submitする際にplaceholderの値を伝送しないようにするために
        		//値を削除してsubmitする。
        		$('.text').each(function() {
        			if ($(this).val() == $(this).attr('data-placeholder')) {
        				$(this).val('');
        			}
        		});
        		
        		form.submit();
        	},
        	groups: getDataValidateGroups()
        });
        
        //Checkbox
		jQuery.validator.addMethod("requiredCheckBox", function(value, element) {
			//data-group-name
			var groupName = $(element).attr("data-validate-group");
			return $("input[data-validate-group='" + groupName + "']:checked").size() > 0;
		});
		jQuery.validator.addClassRules("requiredCheckBox", {
			requiredCheckBox: true
		});
		
		
		//全角カタカナのみ
		jQuery.validator.addMethod("katakana", function(value, element) {
			return this.optional(element) || /^([ァ-ヶー]+)$/.test(value);
		});
		jQuery.validator.addClassRules("katakana", {
			katakana: true
		});

        jQuery.validator.addMethod('phone', function(phone_number, element) {
            phone_number = phone_number.replace(/\s+/g, ''); 
            return this.optional(element) || phone_number.length > 9 &&
                phone_number.match(/^0\d{1,4}-\d{1,4}-\d{3,4}$/);
        });
        
        jQuery.validator.addClassRules("phone", {
        	phone: true,
			maxlength: 14
    	});
    
        // フォームがresetされたときの処理
        $form.bind('reset', function () {
            // Validatorをリセットします
            validator.resetForm();
            // フォーム内のすべてのform-groupからerrorClassとvalidClassを削除します
            $form.find('.form-group').each(function (index, element) {
                $(element).removeClass($.validator.defaults.errorClass).removeClass($.validator.defaults.validClass);
            });
        });
    });

});
/**
 * jQuery Validateのエラーメッセージを作成する。
 * @param form 該当のフォーム
 * @param targetId　ターゲットの入力欄のID
 * @param message 表示するメッセージ
 * @returns {Boolean}
 */
function showValidateErrorMessage(form, targetId, message) {
	
	var $validator = form.validate();
	var obj = new Object();
	obj[targetId] = message;
    $validator.showErrors(obj);  
    
    obj = null;
    $validator = null;
}


// ***************************
//  COMMON AJAX
// ***************************
var cAjax = {
	doSubmit : function(options) {
		
		
		var type = options.type == null ? "post" : options.type;
		var dataType = options.dataType == null ? "json" : options.dataType;
		var async = options.async == null ? true : options.async;
		var formId = options.formId == null ? "mainForm" : options.formId;
		var url = options.url == null ? "" : options.url;
		var errMsgId = options.errMsgId == null ? "errorMsg" : options.errMsgId;
		var func = jQuery.isFunction(options.func) ? options.func : {};
		var isIndicator = options.isIndicator == null ? true : options.isIndicator;
		
		if (url == "") {
			cAjax.errFunc("");
			return;
		}
		if (isIndicator) {
			$("#pleaseWaitDialog").modal("show");
		}
		
		$.ajax({
			type : type,
			dataType : dataType,
			async : async,
			cache : false,
			data : $("#" + formId).serialize(),
			url : url,
			error : function(xhr, status, error) {
				cAjax.errFunc(xhr.status, xhr.responseText);
			},
			complete : function(xhr, status) {
				if (isIndicator) {
					$("#pleaseWaitDialog").modal("hide");
				}
			},
			success : function(data) {
				func(data, formId);
			}
		});
	},
	errFunc : function(status, msg) {
		viewErrorMessage(msg);
	}
};

function viewErrorMessage(msg) {
	alert(msg);
}

/**
 * GetAjax
 * @param url
 * @param nextFunc
 * @returns
 */
function gotoGetAjaxDialog(url, resultFunc) {
	//detail
	cAjax.doSubmit({
		type : "GET",
		url : url,
		func: resultFunc
	});
}

/**
 * post action ajax
 * check form valid
 * @param pFormId
 * @param pageUrl
 * @param resultFunc
 * @returns {Boolean}
 */
function gotoPostActionAjax(pFormId, pageUrl, resultFunc) {
	var validResult = $('#' + pFormId).valid();
	if (validResult) {
		cAjax.doSubmit({
			type : "POST",
			url : pageUrl,
			formId : pFormId, 
			func: (resultFunc ? resultFunc : gotoMemModifyActionSuccess)
		});
	}
	return false;
}

function gotoMemModifyActionSuccess(response, formId) {
	if (response.status == "SUCCESS") {
		location.reload();
	} else {
		if (response.errorMessageLists) {
			var popupErrorMessage = "";
			
			for (inx = 0; inx < response.errorMessageLists.length; inx ++) {
				var errorData = response.errorMessageLists[inx];
				if (document.getElementById(errorData.field) != null) {
					showValidateErrorMessage($("#" +formId), errorData.field, errorData.defaultMessage);
				} else {
					//該当のIDが無い場合,まとめて表示する。
					popupErrorMessage = popupErrorMessage + errorData.defaultMessage + "<br/>";
				}
			}
			if (popupErrorMessage && popupErrorMessage.length > 0) {
				viewErrorMessage("alertDialogCustomMessage", popupErrorMessage);
			}
		} else {
			viewErrorMessage("alertDialogDefaultMessage");
		}
	}
}


function memberDetailResult(response) {

	if (response && response.status == "SUCCESS") {
		
		frontMember = response.model.tFrontMember;
		$("#memberDetailDialog #frontMemberLoginId").attr('readonly', true);
		
		$("#memberDetailDialog #frontMemberKey").val(frontMember.frontMemberKey);
		$("#memberDetailDialog #frontMemberLoginId").val(frontMember.frontMemberLoginId);
		$("#memberDetailDialog #frontMemberLoginDt").html(frontMember.frontMemberLoginDt);
		$("#memberDetailDialog #frontMemberRegDt").html(frontMember.frontMemberRegDt);
		$("#memberDetailDialog #frontMemberName").val(frontMember.frontMemberName);
		
		if (frontMember.frontMemberIsLocked) {
			$("#memberDetailDialog #frontMemberIsLocked1").parent().addClass("active");
			$("#memberDetailDialog #frontMemberIsLocked0").parent().removeClass("active");
			$("#memberDetailDialog #frontMemberIsLocked1").attr("checked", "checked");
		} else {
			$("#memberDetailDialog #frontMemberIsLocked0").parent().addClass("active");
			$("#memberDetailDialog #frontMemberIsLocked1").parent().removeClass("active");
			$("#memberDetailDialog #frontMemberIsLocked0").attr("checked", "checked");
		}
		
		if (frontMember.frontMemberStatusTp) {
			$("#memberDetailDialog #frontMemberStatusTp").selectpicker('val', frontMember.frontMemberStatusTp);
		}
		
		if (frontMember.frontMemberTp) {
			$("#memberDetailDialog #frontMemberTp").selectpicker('val', frontMember.frontMemberTp);
		}
		
		$("#memberDetailDialog").modal("show");
	} else {
		viewErrorMessage("alertDialogDefaultMessage");
	}
}

function showMemberEditDialog() {
	$("#memberDetailDialog #memberDetailDialogForm")[0].reset();
	$("#memberDetailDialog #frontMemberLoginId").attr('readonly', false);
	$("#memberDetailDialog").modal("show");
}