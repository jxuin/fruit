<span class="cart" ><a id="cart_a" rel="external" data-ajax="false" href="cart.php"><img src="../resources/images/cart.png" /><span class="cartnum"></span></a></span>
<span class="search"><img src="../resources/images/search.png" /></span>
<script type="text/javascript">

$(document).bind("mobileinit", function() { 
    $.mobile.autoInitializePage = false; 
});

$(document).ready(function(){
	var fruitnum = parseInt(getCartnum(''));
	if (fruitnum <= 0) {
		$(".cartnum").hide();
	} else {
		$(".cartnum").html(fruitnum);
	}
});
</script>