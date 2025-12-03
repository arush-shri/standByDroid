import { Pause, Play, SkipBack, SkipForward } from "lucide-react-native";
import { useEffect, useRef, useState } from "react";
import { Image, Text, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";
import { GetRandomColor } from "../../context/Randomizer";
import { useUserPreferences } from "../../context/UserPreference";
import {
	GetCurrTrack,
	PauseMedia,
	PlayMedia,
	SendCommand,
} from "../MediaController";
import { Seeker } from "../Seeker";

const One = ({}) => {
	const [track, setTrack] = useState(null);
	const { userPref } = useUserPreferences();
	const [color, setColor] = useState("hsla(36, 81%, 61%, 1.00)");
	const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });
	const reactSize = Math.min(boxSize.width * 0.5, boxSize.height);
	const timeoutRef = useRef(null);

	const getData = async () => {
		if (timeoutRef) clearTimeout(timeoutRef.current);
		const data = await GetCurrTrack();
		setTrack(data === "none" ? null : data);
		if (data !== "none") {
			timeoutRef.current = setTimeout(() => {
				getData();
			}, data.duration);
		}
	};

	useEffect(() => {
		getData();
	}, []);

	useEffect(() => {
		const interval = setInterval(() => {
			const randColor = GetRandomColor();
			setColor(randColor);
		}, userPref?.Randomness || 5000);

		return () => clearInterval(interval);
	}, [userPref]);

	if (!track) {
		return (
			<DriftingView
				onLayout={(e) => {
					const { width, height } = e.nativeEvent.layout;
					setBoxSize({ width, height });
				}}
				styling={styles.container}
			>
				<Text
					style={{
						color: color,
						fontSize: reactSize * 0.2,
						fontFamily: "rdr",
					}}
				>
					No Media Playing
				</Text>
			</DriftingView>
		);
	}

	return (
		<DriftingView
			onLayout={(e) => {
				const { width, height } = e.nativeEvent.layout;
				setBoxSize({ width, height });
			}}
			styling={styles.container}
		>
			{track.artwork ? (
				<Image source={{ uri: track.artwork }} style={styles.artwork} />
			) : null}

			<Text
				style={{
					fontFamily: "naruto",
					color,
					fontSize: reactSize * 0.2,
				}}
			>
				{track.title || "Unknown Title"}
			</Text>
			<Text
				style={{
					fontFamily: "naruto",
					color,
					fontSize: reactSize * 0.1,
					marginTop: reactSize * 0.02,
				}}
			>
				{track.artist || "Unknown Artist"}
			</Text>

			<View
				style={[
					styles.controls,
					{
						gap: reactSize * 0.1,
						marginTop: reactSize * 0.08,
					},
				]}
			>
				<SkipBack
					size={reactSize * 0.2}
					onPress={async () => {
						const res = await SendCommand("previous");
						if (res) setTimeout(() => getData(), 1000);
					}}
					color={color}
				/>

				{track.state === 3 ? (
					<Pause
						size={reactSize * 0.2}
						onPress={async () => {
							const res = await PauseMedia();
							if (res) setTimeout(() => getData(), 1000);
						}}
						color={color}
					/>
				) : (
					<Play
						size={reactSize * 0.2}
						onPress={async () => {
							const res = await PlayMedia();
							if (res) {
								setTimeout(() => getData(), 1000);
							}
						}}
						color={color}
					/>
				)}

				<SkipForward
					size={reactSize * 0.2}
					onPress={async () => {
						const res = await SendCommand("next");
						if (res) setTimeout(() => getData(), 1000);
					}}
					color={color}
				/>
			</View>
			<Seeker reactSize={reactSize} color={color} callback={getData} />
		</DriftingView>
	);
};

const styles = StyleSheet.create({
	container: {
		backgroundColor: "#000",
		alignItems: "center",
	},
	controls: {
		flexDirection: "row",
		alignItems: "center",
	},
});

export default One;
