import Slider from "@react-native-community/slider";
import { useEffect, useRef, useState } from "react";
import { Text, View } from "react-native";
import { GetPosition, SeekTo } from "./MediaController";

function formatTime(ms) {
	if (!ms) return "0:00";
	const totalSeconds = Math.floor(ms / 1000);
	const minutes = Math.floor(totalSeconds / 60);
	const seconds = totalSeconds % 60;
	return `${minutes}:${seconds.toString().padStart(2, "0")}`;
}

export const Seeker = ({ reactSize, color, callback }) => {
	const [position, setPosition] = useState(0);
	const [duration, setDuration] = useState(1); // avoid divide by zero
	const [isSeeking, setIsSeeking] = useState(false);
	const timeoutRef = useRef(null);

	useEffect(() => {
		if (timeoutRef) clearTimeout(timeoutRef.current);
		const interval = setInterval(async () => {
			if (!isSeeking) {
				const data = await GetPosition();
				setPosition(data.position);
				setDuration(data.duration - 100);

				if (data.duration - data.position < 1000) callback();
				else {
					timeoutRef.current = setTimeout(() => {
						callback();
					}, data.duration - data.position);
				}
			}
		}, 1000);

		return () => clearInterval(interval);
	}, [isSeeking]);

	return (
		<>
			<Slider
				style={{
					flexDirection: "row",
					width: "100%",
					marginTop: reactSize * 0.08,
				}}
				minimumValue={0}
				maximumValue={duration}
				value={position}
				tapToSeek
				step={1000}
				onSlidingStart={() => setIsSeeking(true)}
				onValueChange={async (value) => {
					await SeekTo(value);
					setPosition(value);
					setIsSeeking(false);
				}}
				minimumTrackTintColor={color}
				maximumTrackTintColor="#FFF"
				thumbTintColor={color}
			/>
			<View
				style={{
					flexDirection: "row",
					justifyContent: "space-between",
					width: "90%",
				}}
			>
				<Text
					style={{
						color: "#BBBBBB",
					}}
				>
					{formatTime(position)}
				</Text>
				<Text
					style={{
						color: "#BBBBBB",
					}}
				>
					{formatTime(duration)}
				</Text>
			</View>
		</>
	);
};
