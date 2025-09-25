import { Text, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";

const One = ({  }) => {
    return(
        <View style={styles.contatiner}>
            <Text style={{fontSize: 18, color: 'red'}}>ONEEEEEEEEEEEEEEEEEEE</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    contatiner: {
        flex: 1,
        backgroundColor: '#000'
    }
});

export default One;