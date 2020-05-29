'use strict';
const AWS = require('aws-sdk');
const axios = require('axios').default;

module.exports = {
  create: async (event, context) => {
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
    if (typeof bodyObj.user === 'undefined') {
      console.error("Missing parameter")
      console.debug("The parammeter is", bodyObj.user)
      return {
        statusCode: 400
      }
    }
    let putParams = {
      TableName: process.env.DYNAMODB_PLAYER_TABLE,
      Item: {
        playerid: `${event.pathParameters.matchid}${bodyObj.user}`,
        matchid: event.pathParameters.matchid,
        userid: bodyObj.user
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
  deletePlayer: async (event, context) => {
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
    if (typeof bodyObj.user === 'undefined') {
      console.error("Missing parameter")
      console.debug("The parammeter is", bodyObj.user)
      return {
        statusCode: 400
      }
    }
    let deleteParams = {
      TableName: process.env.DYNAMODB_PLAYER_TABLE,
      Key: {
        matchid: event.pathParameters.matchid,
        userid: bodyObj.user
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
  deleteMatch: async (event, context) => {
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
    if (typeof bodyObj.matchid === 'undefined') {
      console.error("Missing parameter")
      console.debug("The parammeter is", bodyObj.matchid)
      return {
        statusCode: 400
      }
    }
    let deleteParams = {
      TableName: process.env.DYNAMODB_PLAYER_TABLE,
      Key: {
        matchid: bodyObj.matchid
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
  list: async (event, context) => {
    let scanParams = {
      TableName: process.env.DYNAMODB_PLAYER_TABLE
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
      body: JSON.stringify(scanResults.Items.map(player => {
        return {
          playerid: player.playerid,
          matchid: player.matchid,
          userid: player.userid
        }
      }))
    }
  }
};