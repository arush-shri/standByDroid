import { BatteryMedium, Calendar, Clock, Music, X } from "lucide-react-native";
import {
	Modal,
	Pressable,
	ScrollView,
	Text,
	TouchableOpacity,
} from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";

const ViewPicker = ({ visible, closeModal, setValue }) => {
	const selecteView = (val) => {
		setValue(val);
		closeModal();
	};
	return (
		<Modal
			onRequestClose={closeModal}
			transparent
			visible={visible}
			animationType="fade"
		>
			<Pressable onPress={closeModal} style={styles.modalOverlay}>
				<TouchableOpacity
					onPress={closeModal}
					style={styles.closeButton}
				>
					<X size={scale(20)} color={"rgba(255,255,255,0.8)"} />
				</TouchableOpacity>
				<ScrollView
					showsVerticalScrollIndicator={false}
					style={styles.scroller}
					contentContainerStyle={styles.scrollContainer}
				>
					<Pressable
						style={styles.buttons}
						onPress={() => {
							selecteView("view_1");
						}}
					>
						<Clock
							size={scale(20)}
							color={"rgba(255,255,255,0.8)"}
						/>
						<Text style={styles.text}>Clock Display</Text>
					</Pressable>

					<Pressable
						style={styles.buttons}
						onPress={() => {
							selecteView("view_2");
						}}
					>
						<BatteryMedium
							size={scale(20)}
							color={"rgba(255,255,255,0.8)"}
						/>
						<Text style={styles.text}>Battery Info</Text>
					</Pressable>

					<Pressable
						style={styles.buttons}
						onPress={() => {
							selecteView("view_3");
						}}
					>
						<Calendar
							size={scale(20)}
							color={"rgba(255,255,255,0.8)"}
						/>
						<Text style={styles.text}>Calendar</Text>
					</Pressable>

					<Pressable
						style={styles.buttons}
						onPress={() => {
							selecteView("view_4");
						}}
					>
						<Music
							size={scale(20)}
							color={"rgba(255,255,255,0.8)"}
						/>
						<Text style={styles.text}>Music Control</Text>
					</Pressable>
				</ScrollView>
			</Pressable>
		</Modal>
	);
};

const styles = StyleSheet.create({
	modalOverlay: {
		flex: 1,
		backgroundColor: "rgba(0,0,0,0.5)",
		justifyContent: "center",
		alignItems: "center",
	},
	closeButton: {
		padding: 8,
		backgroundColor: "#1f2937",
		borderRadius: 10000,
		position: "absolute",
		top: 20,
		right: 20,
		zIndex: 10,
		borderWidth: 1,
		borderColor: "#9ca3af",
	},
	buttons: {
		backgroundColor: "#1f2937",
		width: "100%",
		paddingVertical: 17,
		borderWidth: 1,
		borderColor: "#9ca3af",
		justifyContent: "center",
		alignItems: "center",
		borderRadius: 1000,
		marginVertical: 6,
		flexDirection: "row",
		gap: 10,
	},
	text: {
		color: "rgba(255,255,255,0.8)",
		fontSize: 22,
	},
	scrollContainer: {
		justifyContent: "center",
		alignItems: "center",
		paddingVertical: 20,
	},
	scroller: {
		flex: 1,
		minWidth: "50%",
		height: "100%",
	},
});

export default ViewPicker;
