import { useEffect, useState } from "react";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";
import { GetRandomColor } from "../../context/Randomizer";
import { useUserPreferences } from "../../context/UserPreference";

const Two = ({ info }) => {
	const { userPref } = useUserPreferences();
	const [color, setColor] = useState("#56d137ff");
	const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });

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
		></DriftingView>
	);
};

const styles = StyleSheet.create({
	container: {},
});

export default Two;
