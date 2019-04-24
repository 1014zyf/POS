<script language='Javascript'>
	function dayOptionOnChange() {
		if(document.getElementById('dayOptions').value == 'oneDay') {
			document.getElementById('SingleBusinessDateRow').style.display = '';
			document.getElementById('BeginBusinessDateRow').style.display = 'none';
			document.getElementById('EndBusinessDateRow').style.display = 'none';
		}else if(document.getElementById('dayOptions').value == 'dayRange') {
			document.getElementById('SingleBusinessDateRow').style.display = 'none';
			document.getElementById('BeginBusinessDateRow').style.display = '';
			document.getElementById('EndBusinessDateRow').style.display = '';
		}
	}
</script>
<html>
	<form id='genSalesData' name='genSalesData' action='generateSalesData.php' method='Post'>
		<table>
			<tr>
				<td>Day Option:</td>
				<td><select id='dayOptions' name='dayOptions' onChange='dayOptionOnChange();'><option value='oneDay'>One Day</option><option value='dayRange'>Day Range</option></select></td>
			</tr>
			<tr id='SingleBusinessDateRow'>
				<td>Business Date (YYYYMMDD):</td>
				<td><input id='BusinessDate' name='BusinessDate' value='' size=8 maxlength=8></td>
			</tr>
			<tr id='BeginBusinessDateRow' style='display:none;'>
				<td>Begin Business Date (YYYYMMDD):</td>
				<td><input id='BeginBusinessDate' name='BeginBusinessDate' value='' size=8 maxlength=8></td>
			</tr>
			<tr id='EndBusinessDateRow' style='display:none;'>
				<td>End Business Date (YYYYMMDD):</td>
				<td><input id='EndBusinessDate' name='EndBusinessDate' value='' size=8 maxlength=8></td>
			</tr>
			<tr>
				<td><input type='submit' id='Submit' name='Submit' value='Submit'></td>
				<td></td>
			</tr>
		</table>
	</form>
</html>