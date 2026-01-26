import { BlurView } from "expo-blur";
import { X } from "lucide-react-native";
import { useEffect, useState } from "react";
import {
	KeyboardAvoidingView,
	Modal,
	Platform,
	Pressable,
	ScrollView,
	Text,
	TextInput,
	TouchableOpacity,
	View,
} from "react-native";
import DropDownPicker from "react-native-dropdown-picker";
import { SafeAreaView } from "react-native-safe-area-context";
import { scale, StyleSheet } from "react-native-size-scaling";
import EventsEmitter from "../../app/context/EventsEmitter";
import { getCache, setCache } from "../../app/context/Storage";
import { LoadFont, PickFont } from "../FontPicker";
import { ToastMaker } from "../ToastMaker";

const FontSelector = ({ visible, closeModal }) => {
	const [details, setDetails] = useState("");
	const [screen, setScreen] = useState("");
	const [open, setOpen] = useState(false);
	const [items, setItems] = useState([]);
	const [value, setValue] = useState(null);

	useEffect(() => {
		if (!visible) {
			setDetails("");
			setScreen("");
			setOpen(false);
			setValue(null);
		}
		const stored = JSON.parse(getCache("storedUserFonts") || "{}");
		const arr = [];
		for (const i of Object.keys(stored)) {
			arr.push({ label: i, value: i });
		}
		setItems(arr);
	}, [visible]);

	const FontSelect = (name) => {
		if (!screen) {
			ToastMaker("Please select a screen");
			setValue(null);
			return;
		}
		setCache(`${screen}-font`, name);
		EventsEmitter.emit(`${screen}-font`);
		closeModal();
	};

	return (
		<Modal
			visible={visible}
			animationType="fade"
			onRequestClose={closeModal}
			transparent={true}
		>
			<BlurView intensity={1000} tint="dark" style={{ flex: 1 }}>
				<SafeAreaView style={{ flex: 1 }}>
					<KeyboardAvoidingView
						behavior={Platform.OS === "ios" ? "padding" : undefined}
						style={{
							flex: 1,
						}}
					>
						<TouchableOpacity
							onPress={closeModal}
							style={styles.closeButton}
						>
							<X
								size={scale(20)}
								color={"rgba(255,255,255,0.8)"}
							/>
						</TouchableOpacity>
						<ScrollView
							contentContainerStyle={styles.container}
							keyboardShouldPersistTaps="handled"
						>
							<View
								style={{
									flexDirection: "row",
									flexWrap: "wrap",
									gap: scale(10),
									paddingHorizontal: scale(30),
									justifyContent: "center",
									alignItems: "center",
									marginTop: scale(10),
								}}
							>
								<Pressable
									style={[
										styles.buttons,
										{
											...(screen === "clock" && {
												borderWidth: 0,
												backgroundColor: "#2f80ed",
											}),
										},
									]}
									onPress={() => {
										setScreen("clock");
									}}
								>
									<Text style={styles.text}>Clock</Text>
								</Pressable>

								<Pressable
									style={[
										styles.buttons,
										{
											...(screen === "battery" && {
												borderWidth: 0,
												backgroundColor: "#2f80ed",
											}),
										},
									]}
									onPress={() => {
										setScreen("battery");
									}}
								>
									<Text style={styles.text}>Battery</Text>
								</Pressable>

								<Pressable
									style={[
										styles.buttons,
										{
											...(screen === "calendar" && {
												borderWidth: 0,
												backgroundColor: "#2f80ed",
											}),
										},
									]}
									onPress={() => {
										setScreen("calendar");
									}}
								>
									<Text style={styles.text}>Calendar</Text>
								</Pressable>

								<Pressable
									style={[
										styles.buttons,
										{
											...(screen === "music" && {
												borderWidth: 0,
												backgroundColor: "#2f80ed",
											}),
										},
									]}
									onPress={() => {
										setScreen("music");
									}}
								>
									<Text style={styles.text}>Music</Text>
								</Pressable>
							</View>

							<View
								style={{
									flexDirection: "row",
									gap: scale(10),
									paddingHorizontal: scale(0),
									justifyContent: "space-between",
									width: "100%",
								}}
							>
								<TextInput
									style={styles.input}
									placeholder="Enter a name for your font"
									value={details}
									onChangeText={(text) => setDetails(text)}
									placeholderTextColor="#9c9c9cff"
								/>

								<TouchableOpacity
									style={styles.submitButton}
									onPress={async () => {
										if (!screen) {
											ToastMaker(
												"Please select a screen",
											);
											return;
										}
										const res = await PickFont();
										await LoadFont(details, res);
										setCache(`${screen}-font`, details);
										closeModal();
										EventsEmitter.emit(`${screen}-font`);
									}}
								>
									<Text style={styles.submitText}>Add</Text>
								</TouchableOpacity>
							</View>
							{items.length > 0 && (
								<View
									style={{
										flex: 1,
										flexDirection: "row",
										paddingHorizontal: scale(15),
										width: "100%",
										marginTop: scale(10),
									}}
								>
									<DropDownPicker
										open={open}
										value={value}
										items={items}
										setValue={setValue}
										setOpen={setOpen}
										onChangeValue={FontSelect}
										placeholderStyle={{
											color: "#A8A8A8",
											fontSize: scale(14),
										}}
										placeholder="Choose an existing font"
										style={styles.dropdown}
										dropDownContainerStyle={
											styles.dropdownContainer
										}
										dropDownDirection="BOTTOM"
										textStyle={{
											color: "#FFF",
											fontSize: scale(14),
										}}
										arrowIconStyle={{
											tintColor: "#A8A8A8",
										}}
										tickIconStyle={{ tintColor: "#A8A8A8" }}
										listMode={"SCROLLVIEW"}
									/>
								</View>
							)}
						</ScrollView>
					</KeyboardAvoidingView>
				</SafeAreaView>
			</BlurView>
		</Modal>
	);
};

const styles = StyleSheet.create({
	container: {
		backgroundColor: "rgba(0,0,0,0.5)",
		width: "100%",
		height: "100%",
		justifyContent: "space-between",
		alignItems: "center",
	},
	header: {
		flexDirection: "row",
		justifyContent: "space-between",
		alignItems: "flex-start",
		paddingHorizontal: scale(15),
		paddingVertical: scale(15),
		backgroundColor: "#fff",
		borderTopRightRadius: scale(25),
		borderTopLeftRadius: scale(25),
	},
	headerContent: {
		flex: 1,
	},
	title: {
		fontSize: scale(22),
		color: "#212529",
		marginBottom: scale(3),
	},
	subtitle: {
		fontSize: scale(14),
		color: "#6c757d",
	},
	closeButton: {
		width: scale(36),
		height: scale(36),
		justifyContent: "center",
		alignItems: "center",
		borderRadius: 1000,
	},
	input: {
		borderWidth: 1,
		borderColor: "#9ca3af",
		borderRadius: scale(12),
		padding: scale(10),
		minHeight: scale(50),
		marginTop: scale(10),
		marginHorizontal: scale(13),
		fontSize: scale(14),
		color: "white",
		flex: 1,
	},
	submitButton: {
		backgroundColor: "#2f80ed",
		borderRadius: scale(15),
		marginTop: scale(16),
		marginHorizontal: scale(13),
		height: 50,
		width: "10%",
		justifyContent: "center",
		alignItems: "center",
	},
	submitText: {
		color: "#fff",
		fontSize: scale(16),
		textAlign: "center",
	},
	alertText: {
		fontSize: scale(12),
		color: "#ec7878ff",
		marginLeft: scale(14),
		marginTop: scale(4),
	},
	buttons: {
		backgroundColor: "#1f2937",
		flexDirection: "row",
		paddingHorizontal: 20,
		paddingVertical: 7,
		borderWidth: 1,
		borderColor: "#9ca3af",
		justifyContent: "center",
		alignItems: "center",
		borderRadius: 1000,
	},
	text: {
		color: "rgba(255,255,255,1)",
		fontSize: 19,
	},
	dropdown: {
		backgroundColor: "transparent",
		borderWidth: 1,
		borderColor: "#9ca3af",
		borderRadius: 10,
		height: 50,
		justifyContent: "center",
		paddingHorizontal: 10,
	},
	dropdownContainer: {
		backgroundColor: "transparent",
		borderWidth: 1,
		borderColor: "#EBEBEB",
	},
	closeButton: {
		padding: 8,
		backgroundColor: "#1f2937",
		borderRadius: 10000,
		position: "absolute",
		top: 10,
		left: 10,
		zIndex: 10,
		borderWidth: 1,
		borderColor: "#9ca3af",
	},
});

export default FontSelector;
