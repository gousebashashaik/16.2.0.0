function category_selected(obj){
obj.style.background="#FFFFFF";
obj.style.color="#000000";
obj.style.boxShadow='0 10px 0 0 #FFFFFF, 0 -1px 6px 2px #CCCCCC';
// document.getElementById("sub_before_you_go").style.display='block';
// document.getElementById("sub_before_you_go").style.background='#9FE3E0;';
}

function top_7_questions(obj1, obj2) {
obj1.style.background="#F2F2F2";
document.getElementById("faq_list").style.display='block';
obj2.style.backgroundImage="url('Images/Selected_Sub_Cat.png')";
obj2.style.color="#FFFFFF";
obj.style.width*=1.3;
}

function faq_font(obj)
{
obj.style.color='#119CA6';
}

function activateTab(pageId) {
          var tabCtrl = document.getElementById('subcategories');
          var pageToActivate = document.getElementById(pageId);
          for (var i = 0; i < tabCtrl.childNodes.length; i++) {
              var node = tabCtrl.childNodes[i];
              if (node.nodeType == 1) { /* Element */
                  node.style.display = (node == pageToActivate) ? 'block' : 'none';
              }
          }
      }
	  
	  function activateTab1(pageId) {
          var tabCtrl = document.getElementById('faq_list');
          var pageToActivate = document.getElementById(pageId);
          for (var i = 0; i < tabCtrl.childNodes.length; i++) {
              var node = tabCtrl.childNodes[i];
              if (node.nodeType == 1) { /* Element */
                  node.style.display = (node == pageToActivate) ? 'block' : 'none';
              }
          }
      }
	  
	  function activateTab2(pageId) {
          var tabCtrl = document.getElementById('faq_explain');
          var pageToActivate = document.getElementById(pageId);
          for (var i = 0; i < tabCtrl.childNodes.length; i++) {
              var node = tabCtrl.childNodes[i];
              if (node.nodeType == 1) { /* Element */
                  node.style.display = (node == pageToActivate) ? 'block' : 'none';
              }
          }
      }