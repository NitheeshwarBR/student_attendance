const mongoose = require ('mongoose')
const Schema = mongoose.Schema;

const courseSchema = new Schema({
    course_id : {
        type : String , 
        required : true,
        unique:true
    },
    course_name :{
        type:String,
        required:true
    }

});
module.exports = mongoose.model('Course',courseSchema);