import { Text, TouchableOpacity } from "react-native";
import { StyleSheet } from "react-native-size-scaling";

const SettingPanelItem = ({ label, onClick }) => {
	return (
		<TouchableOpacity
			onPress={() => onClick(label)}
			style={[styles.container]}
		>
			<Text style={styles.text}>{label}</Text>
		</TouchableOpacity>
	);
};

const styles = StyleSheet.create({
	container: {
		paddingVertical: 25,
		paddingHorizontal: 50,
		paddingRight: 30,
	},
	text: {
		fontSize: 20,
		fontWeight: "bold",
		color: "#FCE6E6",
	},
});

export default SettingPanelItem;
