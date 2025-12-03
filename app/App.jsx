import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import { NavigationContainer } from "@react-navigation/native";
import { useFonts } from "expo-font";
import { useKeepAwake } from "expo-keep-awake";
import { StatusBar } from "expo-status-bar";
import { LogBox } from "react-native";
import { MenuProvider } from "react-native-popup-menu";
import Main from "./Main";
import Setting from "./Setting";
import { PreferenceProvider } from "./context/UserPreference";

const Tab = createMaterialTopTabNavigator();
LogBox.ignoreAllLogs(true);

export default function App() {
	useKeepAwake();
	const [fontsLoaded] = useFonts({
		avengers: require("../assets/fonts/avengers.ttf"),
		"batman-filled": require("../assets/fonts/batmfilled.ttf"),
		"batman-outlined": require("../assets/fonts/batmoutlined.ttf"),
		"28days": require("../assets/fonts/days28_later.ttf"),
		formula1: require("../assets/fonts/formula1_bold.otf"),
		harrypotter: require("../assets/fonts/harry_potter.ttf"),
		starwars: require("../assets/fonts/star_wars.ttf"),
		transformers: require("../assets/fonts/transformers_font.ttf"),
		ascreed: require("../assets/fonts/assassinCreed.ttf"),
		bo2: require("../assets/fonts/Black_Ops_2.ttf"),
		cod: require("../assets/fonts/CoD.otf"),
		gta: require("../assets/fonts/gta.ttf"),
		naruto: require("../assets/fonts/naruto.ttf"),
		rdr: require("../assets/fonts/rdr.ttf"),
		tlou: require("../assets/fonts/tlou.ttf"),
	});

	return (
		fontsLoaded && (
			<MenuProvider>
				<PreferenceProvider>
					<Navigation />
				</PreferenceProvider>
			</MenuProvider>
		)
	);
}

const Navigation = () => (
	<NavigationContainer>
		<Tab.Navigator
			initialRouteName="Main"
			screenOptions={{
				swipeEnabled: true,
				tabBarStyle: { display: "none" },
				headerShown: false,
			}}
		>
			<Tab.Screen name="Main" component={Main} />
			<Tab.Screen name="Setting" component={Setting} />
		</Tab.Navigator>
		<StatusBar style="auto" hidden={true} />
	</NavigationContainer>
);
