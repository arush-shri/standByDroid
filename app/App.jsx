import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import { NavigationContainer } from '@react-navigation/native';
import { StatusBar } from 'expo-status-bar';
import Main from './Main';
import Setting from './Setting';
import { PreferenceProvider } from './context/UserPreference';

const Tab = createMaterialTopTabNavigator();

export default function App() {
  return (
    <PreferenceProvider>
      <Navigation />
    </PreferenceProvider>
  );
}

const Navigation = () => (
  <NavigationContainer>
    <Tab.Navigator
      initialRouteName='Main'
      screenOptions={{
        swipeEnabled: true,    // enables horizontal swipe
        tabBarStyle: { display: 'none' }, // hide tabs
        headerShown: false,
      }}
    >
      <Tab.Screen name="Setting" component={Setting} />
      <Tab.Screen name="Main" component={Main} />
    </Tab.Navigator>
    <StatusBar style="auto" hidden={true} />
  </NavigationContainer>
)