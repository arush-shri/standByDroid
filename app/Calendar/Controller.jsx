import * as CalendarAPI from "expo-calendar";
import {
	forwardRef,
	useCallback,
	useEffect,
	useImperativeHandle,
	useState,
} from "react";
import { FlatList, Pressable, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import { ToastMaker } from "../../components/ToastMaker";
import { getCache, setCache } from "../context/Storage";
import One from "./faces/One";
import Two from "./faces/Two";

const FacesMap = {
	one: One,
	two: Two,
};

export const Controller = forwardRef(({ storeKey, viewface }, ref) => {
	const [selector, setSelector] = useState(false);
	const [selectedFace, setSelectedFace] = useState(viewface);
	const FaceComponent = FacesMap[selectedFace];
	const [data, setData] = useState({});

	useEffect(() => {
		(async () => {
			const { status } =
				await CalendarAPI.requestCalendarPermissionsAsync();
			if (status === "granted") {
				loadEvents();
			}
		})();
	}, []);

	const loadEvents = async () => {
		const calendars = await CalendarAPI.getCalendarsAsync(
			CalendarAPI.EntityTypes.EVENT
		);
		const now = new Date();
		const future = new Date();
		future.setMonth(future.getMonth() + 1);

		const allEvents = await CalendarAPI.getEventsAsync(
			calendars.map((c) => c.id),
			new Date(now.getFullYear(), now.getMonth() - 1, 1),
			future
		);

		// Group events by date for marking
		const grouped = {};
		allEvents.forEach((ev) => {
			const date = ev.startDate.split("T")[0];
			grouped[date] = { marked: true, dotColor: "#007AFF" };
		});

		setData({ events: allEvents, marked: grouped });
	};

	const changeFace = useCallback(
		(val) => {
			try {
				const boxStr = getCache(storeKey);
				if (boxStr) {
					const box = JSON.parse(boxStr);
					const updated = {
						...box,
						selected: val,
					};
					setSelectedFace(val);
					setSelector(false);
					setCache(storeKey, JSON.stringify(updated));
				}
			} catch (e) {
				console.log("Save box error", e);
			}
		},
		[storeKey]
	);

	const handleTripleTap = () => {
		setSelector(true);
		ToastMaker("Scroll down and tap on any skin you like", "long");
	};
	useImperativeHandle(ref, () => ({
		handleTripleTap,
	}));

	return (
		<View style={styles.container}>
			{selector ? (
				<SelectionView changeFace={changeFace} data={data} />
			) : FaceComponent ? (
				<FaceComponent data={data} />
			) : null}
		</View>
	);
});

const SelectionView = ({ changeFace, data }) => {
	const faceKeys = Object.keys(FacesMap);
	const [containerHeight, setContainerHeight] = useState(0);
	const [containerWidth, setContainerWidth] = useState(0);

	const onLayout = (e) => {
		const { height, width } = e.nativeEvent.layout;
		setContainerHeight(height);
		setContainerWidth(width);
	};

	return (
		<View style={{ flex: 1 }} onLayout={onLayout}>
			{containerHeight > 0 && (
				<FlatList
					data={faceKeys}
					keyExtractor={(item) => item}
					renderItem={({ item }) => (
						<RenderItem
							item={item}
							changeFace={changeFace}
							containerWidth={containerWidth}
							containerHeight={containerHeight}
							data={data}
						/>
					)}
					pagingEnabled
					showsVerticalScrollIndicator={false}
					horizontal={false} // vertical scroll
					snapToAlignment="center"
					decelerationRate="fast"
					getItemLayout={(_, index) => ({
						length: containerHeight,
						offset: containerHeight * index,
						index,
					})}
				/>
			)}
		</View>
	);
};

const RenderItem = ({
	item,
	changeFace,
	containerWidth,
	containerHeight,
	data,
}) => {
	const FaceComponent = FacesMap[item];
	return (
		<Pressable
			style={{
				flex: 1,
				width: containerWidth,
				height: containerHeight,
				paddingHorizontal: containerWidth / 6,
				paddingVertical: containerHeight / 6,
			}}
			onPress={() => changeFace(item)}
		>
			{/* TODO Pass Data */}
			<FaceComponent data={data} />
		</Pressable>
	);
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: "#1b1b1bff",
	},
	selectorContainer: {
		flex: 1,
	},
});
