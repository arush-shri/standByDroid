import AsyncStorage from "@react-native-async-storage/async-storage";
import { useEffect, useState } from "react";
import { Switch, Text, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import { StorePref } from '../app/context/UserPreference';

const SettingToggle = ({ label, valChange, storeKey, desc }) => {
    const [value, setValue] = useState(false)

    useEffect(() => {
        const getVal = async() => {
            const val = await AsyncStorage.getItem(storeKey);
            setValue(val === "true");
        }
        getVal();
    }, [storeKey])

    return(
        <View style={styles.container}>
            <View>
                <Text style={styles.text}>
                    {label}
                </Text>
                <Text style={styles.descText}>
                    {desc}
                </Text>
            </View>
            <Switch
                trackColor={{false: '#B0B0B0', true: '#CFCFCD'}}
                thumbColor={value ? '#F28500' : '#C41E3A'}
                ios_backgroundColor="#3e3e3e"
                onValueChange={(val) => {
                    setValue(val);
                    StorePref(storeKey, val? 'true' : 'false')
                }}
                value={value}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        paddingVertical: 25,
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingRight: 10
    },
    valRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingHorizontal: 10,
    },
    text: {
        fontSize: 15,
        color: '#F4F1BB',
        fontWeight: 'bold'
    },
    descText: {
        fontWeight: 'bold',
        fontSize: 12, 
        color: '#A1968F',
        paddingTop: 5
    },
    slider: {
        width: '100%', 
        height: 40
    },
    value: {
        fontSize: 12,
        color: '#6caf91ff'
    }
});

export default SettingToggle;