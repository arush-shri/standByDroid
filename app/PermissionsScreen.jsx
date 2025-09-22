import { ScrollView, Text } from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";

const PermissionsScreen = () => {
    return (
        <ScrollView 
        showsVerticalScrollIndicator={false}
        alwaysBounceVertical={true}
        contentContainerStyle={styles.contatiner}>
            {/* <SettingToggle 
            label={'Battery Info'}
            storeKey={'battery'}
            desc={'Give us permission to read and display you battery related information'}
            /> */}
            <Text style={{fontSize: scale(20), color: '#F28500'}}> Under construction </Text>
        </ScrollView>
    )
}

const styles = StyleSheet.create({
    contatiner: {
        paddingLeft: 20,
        paddingRight: 5,
    }
});

export default PermissionsScreen;