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
    if (typeof bodyObj.owner === 'undefined'
    || typeof bodyObj.name === 'undefined'
    || typeof bodyObj.size === 'undefined') {
      console.error("Missing parameters")
      console.debug("The parammeters are", (bodyObj.owner, bodyObj.name, bodyObj.size))
      return {
        statusCode: 400
      }
    }
    let playerid = null
    axios.post(`${process.env.TEGMOB_PLAYER_API}/${bodyObj.name}`, {user: bodyObj.owner})
    .then(response => {
      playerid = response.data.playerId
    })
    .catch(error => {
      console.error("Create player error", error)
      return {
        statusCode: 500
      }
    })
    let putParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Item: {
        name: bodyObj.name,
        owner: bodyObj.owner,
        status: "CREATED",
        size: bodyObj.size,
        players: [playerid]
      }
    }
    let putResults = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      putResults = await dynamodb.put(putParams).promise()
    } catch (putError) {
      console.error("Could't create object on table", putError)
      console.debug("The parammeters are", putParams)
      axios.delete(`${process.env.TEGMOB_PLAYER_API}`, {data: {matchid: event.pathParameters.name}})
      .catch(error => {
        console.error("Delete players error", error)
        return {
          statusCode: 500
        }
      })
      return {
        statusCode: 500
      }
    }
    return {
      statusCode: 200
    }
  },
  join: async (event, context) => {
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
    if (typeof bodyObj.userToAdd === 'undefined') {
      console.error("Missing parameters")
      console.debug("The parammeters are", (
        bodyObj.userToAdd))
      return {
        statusCode: 400
      }
    }
    let getParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: {
        name: event.pathParameters.name
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
    if (getResult.Item === null) {
      return {
        statusCode: 404
      }      
    }
    let match = getResult.Item;
    if (match.size - match.players.size() <= 0) {
      return {
        statusCode: 410
      }
    }
    let updateParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: { name: event.pathParameters.name },
      UpdateExpression: "set players=list_append(:player)",
      ExpressionAttributeValues: {}
    }
    let playerid = null;
    axios.post(`${process.env.TEGMOB_PLAYER_API}/${match.name}`, {user: bodyObj.userToAdd})
    .then(response => playerid = response.data.playerId)
    .catch(error => {
      console.error("Create player error", error)
      return {
        statusCode: 400
      }
    })
    updateParams.ExpressionAttributeValues = {
      ":player": playerid
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
      statusCode: 200,
      body: JSON.stringify({playerid: playerid})
    }
  },
  leave: async (event, context) => {
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
    if (typeof bodyObj.userToRemove === 'undefined') {
      console.error("Missing parameters")
      console.debug("The parammeters are", (
        bodyObj.userToAdd))
      return {
        statusCode: 400
      }
    }
    let getParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: {
        name: event.pathParameters.name
      }
    }
    let getResult = {}
    try {
      let dynamodb = new AWS.DynamoDB.DocumentClient()
      getResult = await dynamodb.scan(getParams).promise()
    } catch (getError) {
      console.error("Could't get object from table", getError)
      console.debug("The key is", getParams)
      return {
        statusCode: 500
      }
    }
    if (getResult.Items === null) {
      return {
        statusCode: 404
      }      
    }
    let match = getResult.Item;
    let updateParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: { name: event.pathParameters.name },
      UpdateExpression: "set players=:players",
      ExpressionAttributeValues: {
        ":players": match.players.filter((value, index, arr) => value != bodyObj.userToRemove)
      }
    }
    axios.delete(`${process.env.TEGMOB_PLAYER_API}/${match.name}`, {data: {user: bodyObj.userToRemove}})
    .catch(error => {
      console.error("Delete player error", error)
      return {
        statusCode: 400
      }
    })
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
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: {
        name: event.pathParameters.name
      }
    }
    axios.delete(`${process.env.TEGMOB_PLAYER_API}`, {data: {matchid: event.pathParameters.name}})
    .catch(error => {
      console.error("Delete players error", error)
      return {
        statusCode: 400
      }
    })
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
      TableName: process.env.DYNAMODB_MATCH_TABLE,
      Key: {
        name: event.pathParameters.name
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
    if (getResult.Items === null) {
      return {
        statusCode: 404
      }      
    }
    return {
      statusCode: 200,
      body: JSON.stringify({
        name: getResult.Item.name,
        owner: getResult.Item.owner,
        status: getResult.Item.status,
        size: getResult.Item.size,
        players: getResult.Item.players
      })
    }
  },
  list: async (event, context) => {
    let scanParams = {
      TableName: process.env.DYNAMODB_MATCH_TABLE
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
      body: JSON.stringify(scanResults.Items.map(match => {
        return {
          name: match.name,
          owner: match.owner,
          status: match.status,
          size: match.size,
          players: match.players
        }
      }))
    }
  }
};
