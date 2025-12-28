import { useEffect, useState } from "react";
import { ScrollView, Text, View } from "react-native";
import {
	Menu,
	MenuOption,
	MenuOptions,
	MenuTrigger,
} from "react-native-popup-menu";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";
import EventsEmitter from "../../context/EventsEmitter";
import { GetRandomColor } from "../../context/Randomizer";
import { getCache } from "../../context/Storage";
import { useUserPreferences } from "../../context/UserPreference";

const One = ({ data }) => {
	const today = new Date();
	const year = today.getFullYear();
	const month = today.getMonth(); // 0-based

	const { userPref } = useUserPreferences();
	const daysInMonth = new Date(year, month + 1, 0).getDate();
	const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });
	const [color, setColor] = useState("#C0C0C0");

	const scaled5 = Math.min(boxSize.width * 0.006, boxSize.height * 0.012);
	const scaled10 = Math.min(boxSize.width * 0.012, boxSize.height * 0.024);
	const [fontFamily, setFontFam] = useState(getCache("calendar-font"));

	useEffect(() => {
		const changeFont = () => {
			const res = getCache("calendar-font");
			if (res) {
				setFontFam(res);
			}
		};
		EventsEmitter.on("calendar-font", changeFont);

		return () => {
			EventsEmitter.off("calendar-font", changeFont);
		};
	}, []);

	useEffect(() => {
		const interval = setInterval(() => {
			const randColor = GetRandomColor();
			setColor(randColor);
		}, userPref?.Randomness || 5000);

		return () => clearInterval(interval);
	}, [userPref]);

	const getDateKey = (day) =>
		`${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(
			2,
			"0"
		)}`;
	const events = data?.events || {};
	const todayKey = `${year}-${String(month + 1).padStart(2, "0")}-${String(
		today.getDate()
	).padStart(2, "0")}`;

	const RenderDays = () => {
		return Array.from({ length: daysInMonth }, (_, i) => {
			const day = i + 1;
			const key = getDateKey(day);
			const eventCount = events[key]?.length || 0;
			const todayColor = GetRandomColor();

			const isToday = todayKey === key;

			return (
				<Menu key={day}>
					<MenuTrigger
						customStyles={{
							triggerWrapper: styles.dayCell,
						}}
					>
						<Text
							style={[
								{
									color: isToday ? todayColor : color,
									fontSize: Math.min(
										boxSize.width * 0.06,
										boxSize.height * 0.12
									),
									fontFamily,
								},
							]}
						>
							{day}
						</Text>

						{/* Dots */}
						<View
							style={[
								styles.dotContainer,
								{
									gap: Math.min(
										boxSize.width * 0.005,
										boxSize.height * 0.01
									),
								},
							]}
						>
							{Array(eventCount)
								.fill(0)
								.map((_, i) => (
									<View
										key={i}
										style={{
											height: Math.min(
												boxSize.width * 0.01,
												boxSize.height * 0.02
											),
											width: Math.min(
												boxSize.width * 0.01,
												boxSize.height * 0.02
											),
											backgroundColor: isToday
												? todayColor
												: color,
											borderRadius: 1000,
										}}
									/>
								))}
						</View>
					</MenuTrigger>

					<MenuOptions
						optionsContainerStyle={{
							borderRadius: scaled10,
							justifyContent: "center",
							paddingHorizontal: scaled10,
							paddingVertical: scaled5,
							width: boxSize.width,
							backgroundColor: "rgba(0, 0, 0, 0.8)",
						}}
					>
						<MenuOption>
							<ScrollView>
								{events[key]?.map((ev, i) => {
									const listClr = GetRandomColor();
									return (
										<View key={i} style={styles.eventItem}>
											<Text
												style={{
													fontFamily,
													fontSize: Math.min(
														boxSize.width * 0.045,
														boxSize.height * 0.09
													),
													color: listClr,
												}}
											>
												{ev.title}
											</Text>
											<Text
												style={{
													fontFamily,
													fontSize: Math.min(
														boxSize.width * 0.045,
														boxSize.height * 0.09
													),
													color: listClr,
												}}
											>
												{ev.startDate}
											</Text>
										</View>
									);
								}) || (
									<Text
										style={{
											fontFamily,
											fontSize: Math.min(
												boxSize.width * 0.06,
												boxSize.height * 0.12
											),
											color: "#222",
										}}
									>
										No events
									</Text>
								)}
							</ScrollView>
						</MenuOption>
					</MenuOptions>
				</Menu>
			);
		});
	};

	return (
		<DriftingView
			onLayout={(e) => {
				const { width, height } = e.nativeEvent.layout;
				setBoxSize({ width, height });
			}}
			styling={styles.contatiner}
		>
			<Text
				style={[
					styles.monthTitle,
					{
						color: color,
						fontSize: Math.min(
							boxSize.width * 0.08,
							boxSize.height * 0.16
						),
						marginBottom: Math.min(
							boxSize.width * 0.03,
							boxSize.height * 0.06
						),
						fontFamily,
					},
				]}
			>
				{today.toLocaleString("default", { month: "long" })} {year}
			</Text>

			<View
				style={[
					styles.calendarGrid,
					{
						gap: Math.min(
							boxSize.width * 0.05,
							boxSize.height * 0.1
						),
					},
				]}
			>
				{RenderDays()}
			</View>
		</DriftingView>
	);
};

const styles = StyleSheet.create({
	contatiner: {},
	monthTitle: {
		textAlign: "center",
		alignSelf: "center",
	},
	calendarGrid: {
		flexDirection: "row",
		flexWrap: "wrap",
	},
	dayCell: {
		alignItems: "center",
		justifyContent: "center",
	},
	dayText: {
		fontFamily: "rdr",
	},
	dotContainer: {
		flexDirection: "row",
		flexWrap: "wrap",
		justifyContent: "center",
	},
	dot: {
		borderRadius: 500,
	},
	modalOverlay: {
		flex: 1,
		backgroundColor: "rgba(0,0,0,0.3)",
		justifyContent: "center",
		alignItems: "center",
	},
	eventPopup: {
		width: "85%",
		backgroundColor: "#fff",
		borderRadius: 10,
		padding: 15,
		maxHeight: "50%",
	},
	popupTitle: {
		fontSize: 18,
		fontWeight: "700",
		marginBottom: 10,
	},
	eventItem: { flex: 1 },
});

export default One;
