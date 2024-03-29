#
# Autogenerated by Thrift
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#

require 'thrift'
require 'matchmaker_types'

    module Matchmaker
      module MatchmakerService
        class Client
          include ::Thrift::Client

          def echocheck(echostring)
            send_echocheck(echostring)
            return recv_echocheck()
          end

          def send_echocheck(echostring)
            send_message('echocheck', Echocheck_args, :echostring => echostring)
          end

          def recv_echocheck()
            result = receive_message(Echocheck_result)
            return result.success unless result.success.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'echocheck failed: unknown result')
          end

          def getmatchingelements(fromstring, tostring, timespan)
            send_getmatchingelements(fromstring, tostring, timespan)
            return recv_getmatchingelements()
          end

          def send_getmatchingelements(fromstring, tostring, timespan)
            send_message('getmatchingelements', Getmatchingelements_args, :fromstring => fromstring, :tostring => tostring, :timespan => timespan)
          end

          def recv_getmatchingelements()
            result = receive_message(Getmatchingelements_result)
            return result.success unless result.success.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getmatchingelements failed: unknown result')
          end

        end

        class Processor
          include ::Thrift::Processor

          def process_echocheck(seqid, iprot, oprot)
            args = read_args(iprot, Echocheck_args)
            result = Echocheck_result.new()
            result.success = @handler.echocheck(args.echostring)
            write_result(result, oprot, 'echocheck', seqid)
          end

          def process_getmatchingelements(seqid, iprot, oprot)
            args = read_args(iprot, Getmatchingelements_args)
            result = Getmatchingelements_result.new()
            result.success = @handler.getmatchingelements(args.fromstring, args.tostring, args.timespan)
            write_result(result, oprot, 'getmatchingelements', seqid)
          end

        end

        # HELPER FUNCTIONS AND STRUCTURES

        class Echocheck_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          ECHOSTRING = 1

          FIELDS = {
            ECHOSTRING => {:type => ::Thrift::Types::STRING, :name => 'echostring'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class Echocheck_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::STRING, :name => 'success'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class Getmatchingelements_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          FROMSTRING = 1
          TOSTRING = 2
          TIMESPAN = 3

          FIELDS = {
            FROMSTRING => {:type => ::Thrift::Types::STRING, :name => 'fromstring'},
            TOSTRING => {:type => ::Thrift::Types::STRING, :name => 'tostring'},
            TIMESPAN => {:type => ::Thrift::Types::I32, :name => 'timespan'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class Getmatchingelements_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::STRING, :name => 'success'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

      end

    end
