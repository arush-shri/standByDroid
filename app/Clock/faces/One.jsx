import { memo, useEffect, useState } from "react";
import { Text } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";
import EventsEmitter from "../../context/EventsEmitter";
import { GetRandomColor } from "../../context/Randomizer";
import { getCache } from "../../context/Storage";
import { useUserPreferences } from "../../context/UserPreference";

const One = memo(({ time }) => {
	const { userPref } = useUserPreferences();
	const formattedTime = time.toLocaleTimeString([], {
		hour: "numeric",
		minute: "2-digit",
		hour12: true,
	});
	const [hourMinute, ampm] = formattedTime.split("\u202F");
	const [color, setColor] = useState("#4582C0");
	const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });
	const [fontFamily, setFontFam] = useState(getCache("clock-font"));

	useEffect(() => {
		const changeFont = () => {
			const res = getCache("clock-font");
			if (res) {
				setFontFam(res);
			}
		};
		EventsEmitter.on("clock-font", changeFont);

		return () => {
			EventsEmitter.off("clock-font", changeFont);
		};
	}, []);

	useEffect(() => {
		const interval = setInterval(() => {
			const randColor = GetRandomColor();
			setColor(randColor);
		}, userPref?.Randomness || 5000);

		return () => clearInterval(interval);
	}, [userPref]);

	return (
		<DriftingView
			onLayout={(e) => {
				const { width, height } = e.nativeEvent.layout;
				setBoxSize({ width, height });
			}}
			styling={styles.container}
		>
			<Text
				style={[
					styles.hourMinute,
					{
						color: color,
						fontSize: Math.min(
							boxSize.width * 0.3,
							boxSize.height * 0.8
						),
						fontFamily,
					},
				]}
			>
				{hourMinute}
			</Text>
			<Text
				style={[
					styles.ampm,
					{
						color: color,
						fontSize: Math.min(
							boxSize.width * 0.15,
							boxSize.height * 0.4
						),
						fontFamily,
					},
				]}
			>
				{ampm?.toUpperCase()}
			</Text>
		</DriftingView>
	);
});

const styles = StyleSheet.create({
	container: {
		flexDirection: "row",
		alignItems: "flex-end",
	},
	hourMinute: {
		fontSize: 90,
	},
	ampm: {
		fontSize: 25,
		marginLeft: 8,
	},
});

export default One;
