import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import { NavigationContainer } from "@react-navigation/native";
import { useFonts } from "expo-font";
import { useKeepAwake } from "expo-keep-awake";
import { StatusBar } from "expo-status-bar";
import { LogBox } from "react-native";
import { MenuProvider } from "react-native-popup-menu";
import Main from "./Main";
import Setting from "./Setting";
import { getCache } from "./context/Storage";
import { PreferenceProvider } from "./context/UserPreference";

const Tab = createMaterialTopTabNavigator();
LogBox.ignoreAllLogs(true);

export default function App() {
	useKeepAwake();
	const stored = JSON.parse(getCache("storedUserFonts") || "{}");
	const [fontsLoaded] = useFonts(stored);

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
