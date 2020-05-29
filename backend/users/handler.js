'use strict';
const AWS = require('aws-sdk');

module.exports = {
  register: async (event, context) => {
    let bodyObj = {}
    try {
      bodyObj = JSON.parse(event.body)
    } catch (jsonError) {
      console.error("The mesage body was wrong", jsonError)
      console.debug("The mesage body is", event.body)
      return {
        statusCode: 400
      }
    }
    if (typeof bodyObj.username === 'undefined'
    || typeof bodyObj.password === 'undefined'
    || typeof bodyObj.firstname === 'undefined'
    || typeof bodyObj.lastname === 'undefined') {
      console.error("Missing parameters")
      console.debug("The parammeters are", (
        bodyObj.username,
        bodyObj.password,
        bodyObj.firstname,
        bodyObj.lastname
      ))
      return {
        statusCode: 400
      }
    }
    let putParams = {
      TableName: process.env.DYNAMODB_USERS_TABLE,
      Item: {
        username: bodyObj.username,
        firstname: bodyObj.firstname,
        lastname: bodyObj.lastname,
        password: bodyObj.password
      }
    }
    let putResults = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      putResults = await dynamodb.put(putParams).promise()
    } catch (putError) {
      console.error("Could't create object on table", putError)
      console.debug("The parammeters are", putParams)
      return {
        statusCode: 500
      }
    }
    return {
      statusCode: 200
    }
  },
  update: async (event, context) => {
    let bodyObj = {}
    try {
      bodyObj = JSON.parse(event.body)
    } catch (jsonError) {
      console.error("The mesage body was wrong", jsonError)
      console.debug("The mesage body is", event.body)
      return {
        statusCode: 400
      }
    }
    if (typeof bodyObj.password === 'undefined'
    || typeof bodyObj.firstname === 'undefined'
    || typeof bodyObj.lastname === 'undefined') {
      console.error("Missing parameters")
      console.debug("The parammeters are", (
        bodyObj.password,
        bodyObj.firstname,
        bodyObj.lastname
      ))
    }
    let updateParams = {
      TableName: process.env.DYNAMODB_USERS_TABLE,
      Key: { username: event.pathParameters.username },
      UpdateExpression: "set firstname=:firstname, lastname=:lastname, password=:password",
      ExpressionAttributeValues: {
        ":firstname": bodyObj.firstname,
        ":lastname": bodyObj.lastname,
        ":password": bodyObj.password
      }
    }
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      dynamodb.update(updateParams).promise()
    } catch (updateError) {
      console.error("Could't update object on table", updateError)
      console.debug("The parammeters are", updateParams)
      return {
        statusCode: 500
      }
    }
    return {
      statusCode: 200
    }
  },
  delete: async (event, context) => {
    let deleteParams = {
      TableName: process.env.DYNAMODB_USERS_TABLE,
      Key: {
        username: event.pathParameters.username
      }
    }
    let deleteResult = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      deleteResult = await dynamodb.delete(deleteParams).promise()
    } catch (deleteError) {
      console.error("Could't delete object from table", deleteError)
      console.debug("The key is", deleteParams)
      return {
        statusCode: 500
      }
    }
    return {
      statusCode: 200
    }
  },
  get: async (event, context) => {
    let getParams = {
      TableName: process.env.DYNAMODB_USERS_TABLE,
      Key: {
        username: event.pathParameters.username
      }
    }
    let getResult = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      getResult = await dynamodb.get(getParams).promise()
    } catch (getError) {
      console.error("Could't get object from table", getError)
      console.debug("The key is", getParams)
      return {
        statusCode: 500
      }
    }
    if (getResult === null || getResult === {}) {
      return {
        statusCode: 404
      }      
    }
    return {
      statusCode: 200,
      body: JSON.stringify({
        username: getResult.Item.username,
        firstname: getResult.Item.firstname,
        lastname: getResult.Item.lastname
      })
    }
  },
  list: async (event, context) => {
    let scanParams = {
      TableName: process.env.DYNAMODB_USERS_TABLE
    }
    let scanResults = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      scanResults = await dynamodb.scan(scanParams).promise()
    } catch (scanError) {
      console.error("Could't get objects from table", scanError)
      console.debug("The key is", scanParams)
      return {
        statusCode: 500
      }
    }
    if (scanResults.Items === null
    || !Array.isArray(scanResults.Items)
    || scanResults.Items.length === 0) {
      return {
        statusCode: 404
      }      
    }
    return {
      statusCode: 200,
      body: JSON.stringify(scanResults.Items.map(user => {
        return {
          username: user.username,
          firstname: user.firstname,
          lastname: user.lastname
        }
      }))
    }
  }
};
