import { useState } from "react";
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
import { SafeAreaView } from "react-native-safe-area-context";
import { scale, StyleSheet } from "react-native-size-scaling";
import EventsEmitter from "../../app/context/EventsEmitter";
import { setCache } from "../../app/context/Storage";
import { LoadFont, PickFont } from "../FontPicker";

const FontSelector = ({ visible, closeModal }) => {
	const [details, setDetails] = useState("");
	const [screen, setScreen] = useState("");

	return (
		<Modal
			visible={visible}
			animationType="fade"
			onRequestClose={closeModal}
			transparent={true}
		>
			<SafeAreaView style={{ flex: 1 }}>
				<KeyboardAvoidingView
					behavior={Platform.OS === "ios" ? "padding" : undefined}
					style={{
						flex: 1,
					}}
				>
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
							}}
						>
							<Pressable
								style={[
									styles.buttons,
									{
										...(screen === "clock" && {
											borderWidth: scale(2),
											borderColor: "#FFF",
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
											borderWidth: scale(2),
											borderColor: "#FFF",
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
											borderWidth: scale(2),
											borderColor: "#FFF",
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
											borderWidth: scale(2),
											borderColor: "#FFF",
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
						<TextInput
							style={styles.input}
							placeholder="Enter font name"
							value={details}
							onChangeText={(text) => setDetails(text)}
							placeholderTextColor="#9c9c9cff"
						/>

						<TouchableOpacity
							style={styles.submitButton}
							onPress={async () => {
								const res = await PickFont();
								await LoadFont(details, res);
								setCache(`${screen}-font`, details);
								EventsEmitter.emit(`${screen}-font`);
								closeModal();
							}}
						>
							<Text style={styles.submitText}>Add</Text>
						</TouchableOpacity>
					</ScrollView>
				</KeyboardAvoidingView>
			</SafeAreaView>
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
		fontFamily: "Poppins-SemiBold",
		color: "#212529",
		marginBottom: scale(3),
	},
	subtitle: {
		fontSize: scale(14),
		color: "#6c757d",
		fontFamily: "DMSans-Regular",
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
		borderColor: "#ccc",
		borderRadius: scale(12),
		padding: scale(10),
		minHeight: scale(50),
		marginTop: scale(10),
		marginHorizontal: scale(13),
		fontSize: scale(14),
		color: "black",
		fontFamily: "DMSans-Regular",
	},
	submitButton: {
		backgroundColor: "#007BFF",
		padding: scale(9),
		borderRadius: scale(15),
		marginTop: scale(16),
		marginHorizontal: scale(13),
	},
	submitText: {
		color: "#fff",
		fontSize: scale(16),
		textAlign: "center",
		fontFamily: "Poppins-SemiBold",
		paddingTop: scale(2),
	},
	alertText: {
		fontSize: scale(12),
		color: "#ec7878ff",
		marginLeft: scale(14),
		marginTop: scale(4),
		fontFamily: "DMSans-Regular",
	},
	buttons: {
		backgroundColor: "rgba(255,255,255,0.2)",
		flexDirection: "row",
		paddingVertical: 10,
		borderWidth: 1.5,
		borderColor: "rgba(255,255,255,0.5)",
		justifyContent: "center",
		alignItems: "center",
		borderRadius: 17,
		marginVertical: 6,
	},
	text: {
		color: "rgba(255,255,255,0.8)",
		fontSize: 19,
		fontFamily: "bo2",
	},
});

export default FontSelector;
