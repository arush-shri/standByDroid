import { useState } from "react";
import { View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import SettingPanelItem from "../components/SettingPanelItem";
import GeneralScreen from "./GeneralScreen";
import PermissionsScreen from "./PermissionsScreen";

const Setting = () => {
	const [view, setView] = useState("General");
	return (
		<View style={styles.container}>
			<View style={styles.leftPanel}>
				<SettingPanelItem label={"General"} onClick={setView} />
				<SettingPanelItem label={"Permissions"} onClick={setView} />
				<SettingPanelItem label={"Access"} onClick={setView} />
				<SettingPanelItem label={"About"} onClick={setView} />
			</View>
			<View style={styles.rightPanel}>
				<ViewSelect view={view} />
			</View>
		</View>
	);
};

const ViewSelect = ({ view }) => {
	if (view === "General") {
		return <GeneralScreen />;
	}
	if (view === "Permissions") {
		return <PermissionsScreen />;
	}
	if (view === "Access") {
		return <View></View>;
	}
	if (view === "About") {
		return <View></View>;
	}
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: "#202020",
		flexDirection: "row",
	},
	leftPanel: {
		backgroundColor: "#0F0F0F",
		borderColor: "#F28500",
	},
	rightPanel: {
		flex: 1,
	},
});

export default Setting;
